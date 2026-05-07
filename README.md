# AksharaDeepaTutor

A modern, fully-featured educational mobile app for 10th-grade SSLC (Secondary School Leaving Certificate) students in India. Built with Kotlin, Jetpack Compose, and AI-powered study tips using Claude.

## ✨ Features

### 📚 Syllabus Tracker
- Browse chapters organized by subject (Science, Math, Social Studies)
- Track completion progress with visual indicators
- View overall and per-subject progress bars
- Mark chapters as complete and track learning journey

### 🎯 Interactive Quizzes
- **5-question daily quizzes** per chapter with deterministic randomization
- **30-second timer** per question for timed practice
- Real-time feedback with correct/incorrect highlighting
- Detailed answer explanations
- Score calculation and progress tracking
- Quiz history and attempt logging

### 🧠 AI Study Tips
- **Claude 3 Haiku** integration for personalized study recommendations
- AI-generated tips after quiz completion
- Graceful offline fallback when API is unavailable
- AI Coach in Strength Map for performance analysis

### 📊 Strength Map
- Triangular radar chart visualizing subject performance
- Color-coded performance badges (Strong/Developing/Needs Work)
- Per-chapter performance metrics
- Subject-wise statistics and trends

### 🔥 Daily Goals & Streaks
- Set daily learning targets (1-10 questions)
- Track completion with circular progress ring
- Maintain learning streaks
- Personalized topic recommendations

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Database (SQLite)
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **AI API**: Anthropic Claude 3 Haiku
- **Navigation**: Jetpack Navigation Compose
- **Async**: Coroutines + Flow
- **Build System**: Gradle 8.10

## 🎨 Design System

### Colors
- **Primary**: Deep Olive (#1A2517)
- **Secondary**: Soft Sage (#ACC8A2)
- **Subject Accents**:
  - Science: Cyan (#00BCD4)
  - Math: Purple (#9C27B0)
  - Social: Orange (#FF9800)
- **Semantic**: Green (#4CAF50), Red (#F44336), Yellow (#FFC107)

### Typography
- Display: 24sp Bold
- Headline: 20sp SemiBold
- Body: 14sp Regular
- Label: 12sp Medium

## 📱 Architecture

### Layered Architecture
┌─────────────────────────────────────────┐
│ Presentation Layer │
│ (Jetpack Compose UI Screens) │
├─────────────────────────────────────────┤
│ ViewModel Layer │
│ (State Management & Business Logic) │
├─────────────────────────────────────────┤
│ Repository Layer │
│ (Data Abstraction & Coordination) │
├─────────────────────────────────────────┤
│ Data Layer │
│ (Room Database + Retrofit API) │
└─────────────────────────────────────────┘


### Database Schema

**Chapter**
- `id`: Int (Primary Key)
- `subject`: String (SCIENCE, MATH, SOCIAL)
- `title`: String
- `description`: String
- `isCompleted`: Boolean
- `completedAt`: Long (milliseconds)

**QuizQuestion**
- `id`: Int (Primary Key)
- `chapterId`: Int (Foreign Key)
- `questionText`: String
- `optionA/B/C/D`: String
- `correctOption`: String
- `explanation`: String

**QuizAttempt**
- `id`: Int (Primary Key)
- `chapterId`: Int (Foreign Key)
- `score`: Int
- `totalQuestions`: Int
- `attemptedAt`: Long

**UserAnswer**
- `id`: Int (Primary Key)
- `attemptId`: Int (Foreign Key)
- `questionId`: Int (Foreign Key)
- `selectedOption`: String

**DailyProgress**
- `userId`: String
- `date`: Long
- `questionsAttempted`: Int
- `goalsCompleted`: Int

**StreakData**
- `userId`: String
- `currentStreak`: Int
- `lastUpdated`: Long

### Key Classes

- **SyllabusViewModel**: Manages chapter list and completion status
- **QuizViewModel**: Handles quiz state, timer, scoring, and AI tips
- **StrengthViewModel**: Calculates subject performance and provides AI coaching
- **GoalViewModel**: Tracks daily progress and streaks
- **ChapterRepository**: Data access for chapters
- **QuizRepository**: Data access for questions and attempts
- **GoalRepository**: Data access for daily progress
- **AnthropicApiService**: Retrofit service for Claude API

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest)
- Android SDK API 34 (minimum API 21)
- Java 17 or higher
- Kotlin 1.9+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/THARUN-GOWDA-K/Tutor-App.git
   cd Tutor-App/AksharaDeepaTutor


Configure API Key (Optional - for AI features)

Create or update local.properties in the project root:
Without the key: The app will gracefully fall back to offline tips
Obtain a key from Anthropic Console
Build the project

Run on emulator or device

Or use Android Studio's "Run" button
First Launch
App automatically prepopulates database with 225 sample questions
Data is organized into 45 chapters (15 per subject)
Quiz randomization is deterministic daily (same 5 questions per chapter each day)
📖 Usage
Taking a Quiz
Navigate to Quiz Mode tab
Select a subject (Science, Math, or Social)
Choose a chapter
Answer 5 questions within 30 seconds each
Review your score and get AI-powered study tips
Tracking Progress
View Syllabus tab to see all chapters and completion status
Check Strength Map to visualize your performance radar
Review subject-wise statistics and color-coded performance
Setting Daily Goals
Go to Daily Goal tab
Adjust your target (1-10 questions)
See recommended chapters to practice
Build your learning streak
🧠 AI Integration
How It Works
After each quiz, Claude 3 Haiku generates personalized study tips
Tips consider your quiz performance and incorrect answers
Async processing with loading states
Offline fallback with curated tips when API is unavailable
API Configuration
📊 Data Generation
Mock questions are generated via Python scripts:

These scripts generate 225 questions with explanations, distributed across 45 chapters.

🔧 Development
Project Structure
Running Tests
Building Release APK
🐛 Troubleshooting
AI Tips Not Showing
Check: local.properties has valid ANTHROPIC_API_KEY
Fallback: App will show offline tips if key is missing
Verify: API key format is sk-ant-api-XXXXX
Database Issues
Clear: ./gradlew clean to reset build cache
Reset: Uninstall app and reinstall to repopulate database
Debug: Check Logcat for database errors
Quiz Timer Not Working
Solution: Ensure app has sufficient runtime permissions
Check: Device time is accurate
Emulator Performance
Optimize: Use hardware acceleration (HAXM/WHPX)
Increase: Emulator RAM to 2GB or higher
Alternative: Test on physical device for better performance
📝 License
This project is part of an internship educational initiative.

👨‍💻 Author
THARUN GOWDA K
cd c:\Users\tharu\OneDrive\Desktop\Internship Project\AksharaDeepaTutor

# Stage all changes
git add .

# Commit with message
git commit -m "Add comprehensive README documentation and complete modern redesign

- Modern UI with Soft Sage + Deep Olive color palette
- 6 fully functional screens: Syllabus, Quiz, Strength Map, Daily Goals
- AI-powered study tips with Claude 3 Haiku
- 5-question daily quizzes with timer
- Offline-first design with graceful fallback
- Complete database schema with 225 mock questions
- MVVM architecture with Hilt DI
- Jetpack Compose + Material 3 UI"

# Push to main branch
git push origin main