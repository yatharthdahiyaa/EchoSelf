# ✦ EchoSelf — Future Self Conversation AI

> *"A conversation with who you're becoming."*

EchoSelf is an AI-powered mental wellness Android app that allows users to talk to a
simulated version of their **future self** — the same person, just 5 years wiser, calmer,
and more grounded. Built for the **Generative AI Mental Wellness Hackathon** focused on
Indian youth.

---

## 📱 Screenshots

| Welcome | Mood Check | Chat | Journal | Profile |
|---------|------------|------|---------|---------|
| Animated orb onboarding | 5-mood selector | Glassmorphism chat | Session history | Stats & streaks |

---

## ✨ Features

- 🔮 **Future Self AI** — Gemini 2.5 Flash responds as *you*, 5 years wiser
- 🎭 **Mood Check-In** — Select your emotional state before every session
- 💬 **Glass Chat UI** — Apple-style glassmorphism with animated gradient background
- 🌊 **Reflection Chips** — Pre-written prompts to help you open up
- 📓 **Journal** — Auto-saves every conversation with mood, duration & message count
- 👤 **Profile** — Daily streaks, reflection stats, affirmation card
- 🌌 **Animated Background** — Three floating orbs (purple, blue, pink) that move gently
- 💜 **Safe by Design** — Never clinical, never diagnostic, always empathetic

---

## 🧠 Core Philosophy

Instead of giving advice like a therapist, EchoSelf responds as:

> **"You — 5 years wiser."**

The AI is:
- Calm and warm, never preachy
- Reflective, not prescriptive
- Tuned for Indian youth (career pressure, family expectations, identity, loneliness)
- Limited to 800 tokens per response — keeping it conversational, not lecture-like

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM (ViewModel + StateFlow) |
| AI Backend | Google Gemini 2.5 Flash API |
| Navigation | Navigation Compose |
| Async | Kotlin Coroutines |
| Storage | In-memory (JournalViewModel) |
| Build | Gradle KTS |

---

## 📁 Project Structure
EchoSelf/
├── app/src/main/java/com/echoself/app/
│ ├── MainActivity.kt
│ ├── data/
│ │ ├── model/
│ │ │ ├── Message.kt
│ │ │ └── JournalEntry.kt
│ │ └── repository/
│ │ └── ChatRepository.kt
│ ├── ui/
│ │ ├── theme/
│ │ │ ├── Color.kt # Deep Cosmos palette
│ │ │ ├── Theme.kt
│ │ │ └── Type.kt
│ │ ├── components/
│ │ │ ├── AnimatedBackground.kt
│ │ │ ├── GlassCard.kt
│ │ │ ├── ChatBubble.kt
│ │ │ ├── TypingIndicator.kt
│ │ │ ├── BottomNavBar.kt
│ │ │ └── ReflectionChips.kt
│ │ └── screens/
│ │ ├── WelcomeScreen.kt
│ │ ├── MoodCheckScreen.kt
│ │ ├── ChatScreen.kt
│ │ ├── JournalScreen.kt
│ │ └── ProfileScreen.kt
│ └── viewmodel/
│ ├── ChatViewModel.kt
│ └── JournalViewModel.kt
├── app/build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── local.properties # ← API key (never commit)

text

---

🎨 Design System
Color Palette — Deep Cosmos
Token	Hex	Usage
CosmosBlack	#05050F	App background
PurpleVibrant	#8B5CF6	Accent, buttons, active states
BlueAccent	#3B82F6	Gradient partner
GoldAccent	#F59E0B	Highlights, affirmations
Glass2	10% white	Card surfaces
GlassBorder	18% white	Card borders
Glass Effect Recipe
kotlin
Modifier
  .background(Color.White.copy(alpha = 0.10f), RoundedCornerShape(24.dp))
  .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(24.dp))
