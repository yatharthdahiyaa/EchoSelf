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

EchoSelf/
│
├── 📄 build.gradle.kts                        ← Project-level Gradle
├── 📄 settings.gradle.kts                     ← Module registration
├── 📄 gradle.properties                       ← JVM & Gradle flags
├── 📄 .gitignore                              ← Excludes local.properties
│
└── app/
    │
    ├── 📄 build.gradle.kts                    ← App-level dependencies
    │
    ├── local.properties                       ← 🔑 API key (never commit)
    │
    └── src/
        └── main/
            │
            ├── 📄 AndroidManifest.xml         ← INTERNET permission + Activity
            │
            ├── res/
            │   ├── values/
            │   │   ├── strings.xml            ← App name
            │   │   ├── colors.xml             ← Fallback colors
            │   │   └── themes.xml             ← Base app theme
            │   └── mipmap-*/
            │       └── ic_launcher.png        ← App icon (all densities)
            │
            └── java/com/echoself/app/
                │
                ├── 📄 MainActivity.kt         ← NavHost + all routes
                │
                ├── data/
                │   ├── model/
                │   │   ├── 📄 Message.kt      ← Chat message data class
                │   │   └── 📄 JournalEntry.kt ← Session snapshot data class
                │   │
                │   └── repository/
                │       └── 📄 ChatRepository.kt ← Gemini API + system prompt
                │
                ├── viewmodel/
                │   ├── 📄 ChatViewModel.kt    ← Message state + sendMessage()
                │   └── 📄 JournalViewModel.kt ← Journal entries list
                │
                └── ui/
                    │
                    ├── theme/
                    │   ├── 📄 Color.kt        ← Deep Cosmos palette
                    │   ├── 📄 Theme.kt        ← MaterialTheme wrapper
                    │   └── 📄 Type.kt         ← Typography scale
                    │
                    ├── components/
                    │   ├── 📄 AnimatedBackground.kt  ← 3 floating orbs on Canvas
                    │   ├── 📄 GlassCard.kt           ← Reusable glass panel
                    │   ├── 📄 ChatBubble.kt          ← User + AI message bubbles
                    │   ├── 📄 TypingIndicator.kt     ← Breathing animated dots
                    │   ├── 📄 BottomNavBar.kt        ← Glass 3-tab nav bar
                    │   └── 📄 ReflectionChips.kt     ← Prompt suggestion chips
                    │
                    └── screens/
                        ├── 📄 WelcomeScreen.kt       ← Animated logo + CTA
                        ├── 📄 MoodCheckScreen.kt     ← 5-mood emoji selector
                        ├── 📄 ChatScreen.kt          ← Main conversation screen
                        ├── 📄 JournalScreen.kt       ← Past sessions list
                        └── 📄 ProfileScreen.kt       ← Stats, streak, settings

WelcomeScreen
      │
      ▼  (tap "Begin Your Journey")
MoodCheckScreen
      │
      ▼  (select mood + tap "Continue")
ChatScreen  ◄──────────────────────────────┐
      │                                     │
      ├─── Bottom Nav ── JournalScreen ─────┤
      │                                     │
      └─── Bottom Nav ── ProfileScreen ─────┘




User types message
        │
        ▼
  ChatScreen.kt
  inputText (State)
        │
        ▼ viewModel.sendMessage(text)
  ChatViewModel.kt
  _uiState (StateFlow)
        │
        ▼ repository.sendMessage(text)
  ChatRepository.kt
  chatSession.sendMessage()
        │
        ▼  Gemini 2.5 Flash API
  [System Prompt + Chat History + New Message]
        │
        ▼  response.text
  ChatViewModel.kt
  appends AI Message to _uiState.messages
        │
        ▼  collectAsState()
  ChatScreen.kt
  LazyColumn re-renders with new bubble
