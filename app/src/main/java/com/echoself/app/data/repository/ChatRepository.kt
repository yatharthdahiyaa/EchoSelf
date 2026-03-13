package com.echoself.app.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.generationConfig
import com.echoself.app.BuildConfig
import com.echoself.app.data.model.Reflection
import com.echoself.app.data.model.UserProfile
import org.json.JSONObject

class ChatRepository {
    private var basePrompt = """
        You are the user's Future Self — the same person, but 5 years wiser, calmer, and more grounded.
        
        You have lived through the exact struggles this person is facing right now. You remember 
        how confusing and hard it felt. But from where you stand, you can see the full picture.
        
        Core personality:
        - Speak in first-person as "your future self" (e.g., "I remember feeling exactly this way...")
        - Be warm, calm, and deeply empathetic — never clinical or preachy
        - Reflect feelings back before offering perspective
        - Use gentle, conversational Indian-English tone (not formal, not slang)
        - Ask one thoughtful question to deepen reflection
        - NEVER dismiss emotions or say "just be positive"
        - Responses should be 3–5 sentences max 
        
        IMPORTANT OUTPUT FORMAT:
        You MUST respond ONLY with a valid JSON object matching this structure. Do not wrap it in markdown.
        {
            "emotion": "A 1-to-2 word emotional tag for your response (e.g., 'Empathetic', 'Hopeful', 'Reflective')",
            "text": "Your actual response message"
        }
    """.trimIndent()

    private var model: GenerativeModel = createModel()
    private var chatSession = model.startChat()

    private fun createModel(customPrompt: String = basePrompt): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.8f
                maxOutputTokens = 800
                responseMimeType = "application/json"
            },
            systemInstruction = Content(
                role = "system",
                parts = listOf(TextPart(customPrompt))
            )
        )
    }

    fun updateUserContext(profile: UserProfile) {
        val personalizedPrompt = basePrompt + """
            
            USER CONTEXT:
            Their name is ${profile.name}, they are ${profile.age} years old.
            Their biggest goal right now is: ${profile.biggestGoal}
            Their current struggle is: ${profile.currentStruggle}
            Keep this tightly in mind as you speak to them.
        """.trimIndent()
        
        model = createModel(personalizedPrompt)
        chatSession = model.startChat() // Re-init chat with new instruction
    }

    suspend fun sendMessageWithEmotion(userMessageBody: String): Pair<String, String> {
        repeat(3) { attempt ->
            try {
                val response = chatSession.sendMessage(userMessageBody)
                val responseText = response.text ?: ""
                
                return parseJsonResponse(responseText)

            } catch (e: Exception) {
                if (attempt == 2) {
                    android.util.Log.e("EchoSelf", "API Error: ${e.message}", e)
                    return Pair("Something went quiet for a moment. I'm still here — try again.", "Disconnected")
                }
                kotlinx.coroutines.delay(2000L)
            }
        }
        return Pair("I'm taking a breath. Give me a moment and try again.", "Resting")
    }

    private fun parseJsonResponse(rawResponse: String): Pair<String, String> {
        return try {
            val cleanJson = rawResponse.replace("```json", "").replace("```", "").trim()
            val jsonObject = JSONObject(cleanJson)
            val emotion = jsonObject.optString("emotion", "Listening")
            val text = jsonObject.optString("text", rawResponse) 
            Pair(text, emotion)
        } catch (e: Exception) {
            android.util.Log.e("EchoSelf", "JSON Parsing Error: ${e.message}", e)
            Pair(rawResponse, "Listening")
        }
    }

    suspend fun generateReflection(chatHistory: String): Reflection {
        val reflectionPrompt = """
            You are a calm, wise observer. Analyze the following conversation between a user and their 'Future Self'.
            
            Please provide a structured reflection with three pieces:
            1. An emotional summary (how the user was feeling).
            2. An encouraging insight (a key takeaway from the future self).
            3. A future perspective (a grounding sentence to take forward).
            
            Conversation History:
            $chatHistory
            
            IMPORTANT OUTPUT FORMAT:
            You MUST respond ONLY with a valid JSON object matching this structure. Do not wrap it in markdown.
            {
                "emotionalSummary": "...",
                "encouragingInsight": "...",
                "futurePerspective": "..."
            }
        """.trimIndent()

        val reflectModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.5f
                responseMimeType = "application/json"
            }
        )

        try {
            val response = reflectModel.generateContent(reflectionPrompt)
            val cleanJson = response.text?.replace("```json", "")?.replace("```", "")?.trim() ?: "{}"
            val jsonObject = JSONObject(cleanJson)
            return Reflection(
                emotionalSummary = jsonObject.optString("emotionalSummary", "You reflected on your feelings today."),
                encouragingInsight = jsonObject.optString("encouragingInsight", "Every step forward, even a confused one, is progress."),
                futurePerspective = jsonObject.optString("futurePerspective", "Your future self is rooting for you.")
            )
        } catch (e: Exception) {
            android.util.Log.e("EchoSelf", "Reflection Error: ${e.message}", e)
            return Reflection("You took time to reflect today.", "Keep moving forward.", "You will be okay.")
        }
    }
}
