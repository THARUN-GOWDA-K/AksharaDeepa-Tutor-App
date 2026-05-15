<div align="center">

<img src="C:\Users\tharu\OneDrive\Desktop\App Logo.png" width="150" height="150" />

# 🌟 Akshara Deepa Tutor 🌟
### *Lighting the Path to Academic Excellence for SSLC Students*

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)](https://developer.android.com/compose)
[![FastAPI](https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi&logoColor=white)](https://fastapi.tiangolo.com/)
[![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)
[![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org/)

---

**Akshara Deepa Tutor** is a state-of-the-art, AI-augmented self-study application designed for 10th-grade students in rural India. It bridges the gap between traditional learning and modern technology with a premium, glassmorphism UI and personalized AI coaching.

[Explore Features](#-key-features) • [View Architecture](#-system-architecture) • [Setup Guide](#-installation)

</div>

---

## 📸 App Showcase

<div align="center">
  <table border="0">
    <tr>
      <td width="50%"><img src="file:///C:/Users/tharu/.gemini/antigravity/brain/8d6fb6e7-f661-4b93-a135-f7ca2abca25f/akshara_deepa_mockup_1778821079644.png" alt="Syllabus Tracker" /></td>
      <td width="50%"><img src="file:///C:/Users/tharu/.gemini/antigravity/brain/8d6fb6e7-f661-4b93-a135-f7ca2abca25f/akshara_deepa_analytics_1778821101226.png" alt="Strength Map" /></td>
    </tr>
    <tr>
      <td align="center"><b>📚 Smart Syllabus Tracker</b></td>
      <td align="center"><b>📊 Intelligent Strength Map</b></td>
    </tr>
  </table>
</div>

---

## 🚀 Key Features

### 📖 **Comprehensive Syllabus Hub**
*   **Subject Mastery**: Full coverage of Science, Math, and Social Studies.
*   **Progress Visualization**: Dynamic progress rings and bars that update as you learn.
*   **Concept Management**: Break down chapters into manageable concepts.

### ⚡ **Timed Power Quizzes**
*   **30s Timer**: Build speed and confidence for final exams.
*   **Instant Logic**: Immediate feedback with high-quality explanations for every question.
*   **History Tracking**: Review your past attempts and see your growth over time.

### 🤖 **AI Study Coach**
*   **Personalized Tips**: AI analyzes your performance to give you "Next Step" advice.
*   **Performance Analysis**: Ask the AI Coach about your specific strengths and weaknesses.
*   **Offline Fallback**: Intelligent logic ensures study tips are available even without internet.

### 🔥 **Gamified Consistency**
*   **Study Streaks**: Keep your daily learning chain alive with visual streak counters.
*   **Daily Goals**: Set your own target and hit it every day to unlock badges.
*   **Topic Suggestions**: Smart algorithm recommends topics you need to practice most.

---

## 🛠️ Modern Tech Stack

<div align="center">

| 📱 Mobile Frontend | ⚙️ Backend & API | ☁️ Cloud & AI |
| :--- | :--- | :--- |
| **Kotlin** (Modern Android) | **FastAPI** (Python 3) | **Firebase Auth** (Security) |
| **Jetpack Compose** (UI) | **Uvicorn** (ASGI Server) | **Firestore** (Real-time DB) |
| **Room DB** (Local Cache) | **Pydantic** (Validation) | **Gemini / Claude** (Gen AI) |
| **Hilt** (Dependency Injection) | **Firebase Admin SDK** | **Retrofit** (Networking) |

</div>

---

## 🏗️ System Architecture

```mermaid
graph LR
    subgraph "Client Side (Android)"
    UI[Jetpack Compose UI] --> VM[ViewModel Layer]
    VM --> Repo[Repository Layer]
    Repo --> Room[(Room Local DB)]
    end

    subgraph "Backend Layer"
    Repo --> API[FastAPI Gateway]
    API --> Sync[Progress Sync Service]
    API --> AI[AI Coaching Service]
    end

    subgraph "Cloud Services"
    Sync --> Firestore[(Firebase Firestore)]
    AI --> Gemini[Google Gemini API]
    Auth[Firebase Auth] <--> UI
    end

    style UI fill:#f9f,stroke:#333,stroke-width:2px
    style API fill:#bbf,stroke:#333,stroke-width:2px
    style Gemini fill:#dfd,stroke:#333,stroke-width:2px
```

---

## 📥 Installation

### 1️⃣ **Clone & Prep**
```bash
git clone https://github.com/THARUN-GOWDA-K/AksharaDeepa-Tutor-App.git
```

### 2️⃣ **Backend Startup**
```bash
cd backend
python -m venv .venv
.\.venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

### 3️⃣ **Android Launch**
*   Open `AksharaDeepaTutor` in Android Studio.
*   Add `google-services.json` to the `app/` folder.
*   Sync Gradle & Run on Emulator! 🚀

---

<div align="center">

**Built with ❤️ for the future of education in India.**  
© 2026 Akshara Deepa Tutor Platform

</div>
