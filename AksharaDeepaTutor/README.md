# Akshara Deepa Tutor 🎓

A modern, AI-powered tutoring platform for 10th-grade SSLC (Secondary School Leaving Certificate) students in India. Built with Kotlin + Jetpack Compose, Room offline caching, Firebase Auth/Firestore, and a FastAPI backend that powers cloud sync and AI study tips.

**Status**: ✅ Production-Ready Foundation | **Version**: 1.1 | **Last Updated**: May 14, 2026

---

## 📱 Screenshots & UI

### Design System
- **Primary Color**: Deep Olive (#1A2517) - Professional and calm
- **Secondary Color**: Soft Sage (#ACC8A2) - Warm and inviting
- **Subject Accents**: Science (Cyan), Math (Purple), Social Studies (Orange)
- **Modern UI**: Material 3 with Jetpack Compose, rounded corners (12dp), smooth animations

### Screens
1. **Syllabus Tracker** - Browse chapters by subject with progress tracking
2. **Quiz Mode** - Select subject and chapter for quizzes
3. **Quiz Play** - 5-question timed quizzes with real-time feedback
4. **Quiz Summary** - Score display with AI-powered study tips
5. **Strength Map** - Radar chart showing subject performance
6. **Daily Goal** - Streak tracking and daily learning targets

### App Logo
- Place the logo file at `app/src/main/res/drawable/akshara_logo.png`
- Update `SplashScreen` to pass `logoResId = R.drawable.akshara_logo` once the asset is available

---

## ✨ Key Features

### 📚 Syllabus Tracker
- Browse 45 chapters organized by subject (Science, Math, Social Studies)
- Track completion progress with visual progress bars (overall + per-subject)
- Color-coded chapter cards with:
  - Chapter title and key concepts
  - Last quiz score (if attempted)
  - Completion status badges
  - "Mark as Complete" button
- Real-time progress updates

### 🎯 Interactive Quizzes
- **5-question daily quizzes** per chapter with deterministic randomization
- **30-second timer** per question for timed practice
- Real-time feedback:
  - Correct answers highlighted in green with checkmark
  - Incorrect answers highlighted in red with cross
  - Detailed answer explanations
- Score calculation and progress tracking
- Complete quiz history and attempt logging
- Database persistence of all quiz data

### 🧠 AI Study Tips (Claude 3 Haiku)
- **AI Integration**: Anthropic Claude 3 Haiku via REST API
- Personalized study recommendations after each quiz
- AI-generated tips considering your performance and incorrect answers
- **Async Processing**: Loading states and smooth UX
- **Offline Fallback**: Graceful fallback to curated tips when API is unavailable
- **AI Coach**: Ask questions in Strength Map for performance analysis

### 📊 Strength Map
- **Interactive Radar Chart**: Triangular visualization of subject performance
  - Canvas-based rendering with mathematical precision
  - Real-time updates after quiz completion
- **Subject Stat Cards**: Percentage scores for Science, Math, Social
- **Color-Coded Performance Badges**:
  - 🟢 Green: "Strong" (80%+)
  - 🟡 Yellow: "Developing" (60-79%)
  - 🔴 Red: "Needs Work" (0-59%)
  - ⚪ Gray: "Not attempted"
- **AI Coach Modal**: Tap FAB to ask questions and get AI insights

### 🔥 Daily Goals & Streaks
- Set daily learning targets (1-10 questions)
- Circular progress ring showing current/target progress
- 🔥 Streak counter maintaining learning consistency
- Smart color change: DeepOlive → SuccessGreen when goal met
- Personalized topic recommendations based on strength
- "Take Quiz" buttons for quick access to practice

### 🔐 Offline-First Design
- All core functionality works without internet
- AI tips gracefully degrade to offline versions
- Database prepopulation with 225 sample questions
- No data loss on network interruptions

### 🔐 Authentication & Cloud Sync
- Firebase email/password authentication
- Student profile stored in Firestore
- Quiz history and progress synced to cloud
- Room database used as offline cache

---

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Kotlin | 1.9.0 |
| **UI Framework** | Jetpack Compose | 2024.01.00 |
| **Design System** | Material 3 | Latest |
| **Architecture** | MVVM + Repository Pattern | - |
| **Dependency Injection** | Hilt | 2.49 |
| **Database** | Room + SQLite | 2.6.1 |
| **Networking** | Retrofit + OkHttp | 2.9.0 / 4.12.0 |
| **AI API** | FastAPI AI Gateway (Gemini / Claude) | - |
| **Navigation** | Jetpack Navigation Compose | 2.7.6 |
| **Async** | Coroutines + Flow | 1.7.0 |
| **Build System** | Gradle | 8.10 |
| **Target API** | Android 14 (API 34) | Min: API 21 |
| **Java Version** | Java 17 | - |
| **Auth** | Firebase Authentication | - |
| **Cloud DB** | Firebase Firestore | - |
| **Storage** | Firebase Storage | - |
| **Backend** | FastAPI + Firebase Admin | - |

---

## 🎨 Design System

### Color Palette

| Color | Hex Code | Usage |
|-------|----------|-------|
| Deep Olive | #1A2517 | Primary, text, headers |
| Soft Sage | #ACC8A2 | Secondary, backgrounds |
| Science Cyan | #00BCD4 | Science subject accent |
| Math Purple | #9C27B0 | Math subject accent |
| Social Orange | #FF9800 | Social studies accent |
| Success Green | #4CAF50 | Correct answers, success states |
| Error Red | #F44336 | Incorrect answers, errors |
| Warning Yellow | #FFC107 | Warnings, intermediate scores |
| Gray Badge | #BDBDBD | Neutral states, badges |

### Typography

| Style | Size | Weight | Usage |
|-------|------|--------|-------|
| Display | 24sp | Bold | Screen titles |
| Headline | 20sp | SemiBold | Section headers |
| Body | 14sp | Regular | Body text, descriptions |
| Label | 12sp | Medium | Tags, badges, labels |

### Shapes
- **Border Radius**: 12dp rounded corners (all components)
- **Material 3 Compliance**: Smooth, modern appearance

---

## 📱 Architecture

### Layered Architecture Pattern

```
┌─────────────────────────────────────────────────────┐
│         Presentation Layer                          │
│  ├─ Syllabus Tracker Screen                        │
│  ├─ Quiz Mode Selection Screen                     │
│  ├─ Quiz Play Screen                               │
│  ├─ Quiz Summary Screen                            │
│  ├─ Strength Map Screen                            │
│  └─ Daily Goal Screen                              │
├─────────────────────────────────────────────────────┤
│         ViewModel Layer                             │
│  ├─ SyllabusViewModel (State & UI Logic)           │
│  ├─ QuizViewModel (Quiz Flow & AI Tips)            │
│  ├─ StrengthViewModel (Analytics & AI Coach)       │
│  └─ GoalViewModel (Progress & Streaks)             │
├─────────────────────────────────────────────────────┤
│         Repository Layer                            │
│  ├─ ChapterRepository (Chapter CRUD)               │
│  ├─ QuizRepository (Questions & Attempts)          │
│  └─ GoalRepository (Daily Progress)                │
├─────────────────────────────────────────────────────┤
│         Data Layer                                  │
│  ├─ Room Database (SQLite)                         │
│  ├─ Anthropic API (Retrofit)                       │
│  └─ MockDataHelper (Prepopulation)                 │
└─────────────────────────────────────────────────────┘
```

### Data Flow

**Quiz Flow**:
```
QuizMode → Start Quiz → QuizViewModel.startQuiz(chapter)
  ↓
Quiz Play → Answer questions → updateAnswer()
  ↓
Submit Quiz → finishQuiz() → Save to DB
  ↓
Quiz Summary → getAiStudyTips() → Display tips
  ↓
Strength Map → Updates automatically via Flow
```

### Database Schema

#### **Chapter**
```kotlin
@Entity
data class Chapter(
    @PrimaryKey val id: Int,
    val subject: String,                    // SCIENCE, MATH, SOCIAL
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
```

#### **QuizQuestion**
```kotlin
@Entity(foreignKeys = [
    ForeignKey(entity = Chapter::class, parentColumns = ["id"], childColumns = ["chapterId"])
])
data class QuizQuestion(
    @PrimaryKey val id: Int,
    val chapterId: Int,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String,              // A, B, C, or D
    val explanation: String
)
```

#### **QuizAttempt**
```kotlin
@Entity(foreignKeys = [
    ForeignKey(entity = Chapter::class, parentColumns = ["id"], childColumns = ["chapterId"])
])
data class QuizAttempt(
    @PrimaryKey val id: Int,
    val chapterId: Int,
    val score: Int,
    val totalQuestions: Int,
    val attemptedAt: Long                   // milliseconds
)
```

#### **UserAnswer**
```kotlin
@Entity(foreignKeys = [
    ForeignKey(entity = QuizAttempt::class, parentColumns = ["id"], childColumns = ["attemptId"]),
    ForeignKey(entity = QuizQuestion::class, parentColumns = ["id"], childColumns = ["questionId"])
])
data class UserAnswer(
    @PrimaryKey val id: Int,
    val attemptId: Int,
    val questionId: Int,
    val selectedOption: String,             // A, B, C, or D
    val isCorrect: Boolean
)
```

#### **DailyProgress**
```kotlin
@Entity(primaryKeys = ["userId", "date"])
data class DailyProgress(
    val userId: String,
    val date: Long,
    val questionsAttempted: Int,
    val goalsCompleted: Int
)
```

#### **StreakData**
```kotlin
@Entity(primaryKeys = ["userId"])
data class StreakData(
    val userId: String,
    val currentStreak: Int,
    val lastUpdated: Long
)
```

### Key ViewModels

#### **QuizViewModel**
- Manages quiz state across all quiz screens
- 5-question selection logic using `stableSetOf` for daily consistency
- Timer management (30 seconds per question)
- Answer tracking and scoring
- AI tips generation with fallback
- Database persistence

```kotlin
// 5-Question Daily Randomization Logic
fun startQuiz(chapter: Chapter) {
    val questions = stableSetOf(questionIds).take(5).toList()
    // Same 5 questions per chapter every day
}
```

#### **SyllabusViewModel**
- Manages chapter list by subject
- Progress calculation (overall + per-subject)
- Completion status tracking
- Real-time updates via Flow

#### **StrengthViewModel**
- Calculates subject-wise performance
- Generates radar chart data
- AI coach functionality
- Color-coded badge logic

#### **GoalViewModel**
- Daily progress tracking
- Streak management
- Recommended topics
- Goal completion status

### Repositories

- **ChapterRepository**: CRUD operations for chapters
- **QuizRepository**: Question retrieval, attempt saving
- **GoalRepository**: Daily progress and streak data

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Latest version (Koala or newer)
- **Android SDK**: API 34 (minimum API 21 supported)
- **Java**: Version 17 or higher
- **Kotlin**: 1.9.0+
- **Gradle**: 8.10+
- **Git**: For version control

### Installation Steps

#### 1. Clone Repository
```bash
git clone https://github.com/THARUN-GOWDA-K/Tutor-App.git
cd Tutor-App/AksharaDeepaTutor
```

#### 2. Configure API + Backend (Required for cloud sync & AI)

Create `local.properties` in project root:

```properties
# Backend base URL (Android emulator default)
BACKEND_BASE_URL=http://10.0.2.2:8000/

# Optional: Direct Claude key (legacy fallback)
ANTHROPIC_API_KEY=sk-ant-api-XXXXXXXXXXXXX
```

Get API key from [Anthropic Console](https://console.anthropic.com/) or use the backend AI gateway.

#### 3. Firebase Setup

1. Create a Firebase project
2. Enable Authentication (Email/Password)
3. Enable Firestore + Storage
4. Download `google-services.json` and place it in:

```
app/google-services.json
```

#### 4. Sync Gradle Files

```bash
./gradlew clean
./gradlew sync
```

#### 5. Start Backend (FastAPI)

```bash
cd backend
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

#### 6. Build Project

```bash
./gradlew build
```

#### 5. Run on Emulator/Device

**Option A: Android Studio**
- Click "Run" button (green play icon)
- Select target device/emulator
- App will install and launch

**Option B: Command Line**
```bash
./gradlew installDebug
./gradlew install
```

#### 6. First Launch
- App auto-prepopulates database with 225 questions
- Organized into 45 chapters (15 per subject)
- Quiz randomization is deterministic daily
- No additional setup needed!

---

## 📖 User Guide

### Taking a Quiz

1. **Open Quiz Mode Tab**
   - Select subject: Science, Math, or Social Studies
   - Choose a chapter

2. **Answer 5 Questions**
   - 30 seconds per question (timer visible at top)
   - Select answer → immediate visual feedback
   - Green = Correct, Red = Incorrect
   - See explanation after answering

3. **Review Summary**
   - View your score (e.g., "4/5 Correct - 80%")
   - Read AI-generated study tips
   - Review all answers with explanations

### Tracking Your Progress

1. **Syllabus Tab**
   - View all 45 chapters organized by subject
   - See completion status (checkmark badge)
   - Mark chapters complete
   - Track overall progress with bars

2. **Strength Map Tab**
   - Visual radar chart of your performance
   - Color-coded performance for each subject
   - Detailed chapter-wise statistics
   - Ask AI Coach for insights

### Setting Daily Goals

1. **Daily Goal Tab**
   - Adjust target (1-10 questions)
   - See current progress with ring indicator
   - Build your learning streak (🔥)

2. **Track Recommended Topics**
   - Personalized recommendations
   - Quick "Take Quiz" access
   - Practice areas you need help with

### Using AI Coach

- **In Quiz Summary**: Auto-generates tips after each quiz
- **In Strength Map**: Ask custom questions about your performance
- **Offline Mode**: Falls back to curated tips if no internet

---

## 🧠 AI Integration Details

### AI Study Tips (Backend Gateway)

1. **API Setup**
   ```kotlin
   @POST("ai-study-tip")
   suspend fun getAiStudyTip(@Body request: AiTipRequest): AiTipResponse
   ```

2. **Quiz Completion Flow**
   ```kotlin
   fun finishQuiz() {
       // Save quiz data to DB
       // Fetch AI tips from backend
       getAiStudyTips()
   }
   ```

3. **Backend Provider**
   - `AI_PROVIDER=gemini` or `AI_PROVIDER=claude`
   - `AI_API_KEY` set in backend `.env`

4. **Graceful Fallback**
   - If API is unavailable, the app shows curated offline tips.

### Error Handling
- Network timeout → Shows offline tips
- Invalid API key → Falls back gracefully
- Rate limiting → Cached responses
- No crashes → Always provides feedback

---

## 📊 Data Generation

### Mock Data Generation

225 sample questions generated via Python scripts:

```bash
python generate_mock_data.py    # Main generator
python science_qs.py             # Science questions
python math_qs.py                # Math questions
python social_qs.py              # Social studies questions
```

### Question Distribution
- **Total Questions**: 225
- **Subjects**: 3 (Science, Math, Social)
- **Chapters per Subject**: 15
- **Questions per Chapter**: 5
- **Coverage**: SSLC curriculum topics

### Data Structure
```python
{
    "id": 1,
    "chapterId": 1,
    "subject": "SCIENCE",
    "questionText": "What is photosynthesis?",
    "optionA": "...",
    "optionB": "...",
    "optionC": "...",
    "optionD": "...",
    "correctOption": "B",
    "explanation": "Detailed explanation here..."
}
```

---

## 🔧 Development Guide

### Project Structure

```
AksharaDeepaTutor/
├── app/
│   ├── src/main/
│   │   ├── java/com/aksharadeepa/tutor/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── dao/
│   │   │   │   │   ├── database/
│   │   │   │   │   └── entities/
│   │   │   │   ├── remote/
│   │   │   │   │   ├── api/
│   │   │   │   │   └── dto/
│   │   │   │   └── repository/
│   │   │   ├── models/
│   │   │   ├── ui/
│   │   │   │   ├── auth/
│   │   │   │   ├── splash/
│   │   │   │   ├── home/
│   │   │   │   ├── syllabus/
│   │   │   │   ├── quiz/
│   │   │   │   ├── progress/
│   │   │   │   ├── strength/
│   │   │   │   ├── profile/
│   │   │   │   ├── goal/
│   │   │   │   ├── theme/
│   │   │   │   └── navigation/
│   │   │   ├── viewmodel/
│   │   │   ├── utils/
│   │   │   ├── workers/
│   │   │   ├── AksharaDeepaTutorApp.kt      (Hilt Application)
│   │   │   └── MainActivity.kt               (Entry point)
│   │   └── res/
│   │       ├── values/
│   │       │   ├── strings.xml
│   │       │   └── themes.xml
│   │       └── values-night/
│   │           └── colors.xml
│   ├── build.gradle.kts                       (App-level dependencies)
│   └── AndroidManifest.xml
├── backend/
│   ├── app/
│   │   ├── routes/
│   │   ├── schemas/
│   │   ├── services/
│   │   ├── firebase/
│   │   ├── ai/
│   │   └── main.py
│   ├── requirements.txt
│   ├── .env.example
│   └── README.md
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── build.gradle.kts                          (Project-level build config)
├── settings.gradle.kts
├── gradlew & gradlew.bat                     (Gradle wrapper)
├── local.properties                          (Local API keys)
└── README.md                                 (This file)
```

### Building & Testing

#### Clean Build
```bash
./gradlew clean build
```

#### Build Debug APK
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

#### Build Release APK
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

#### Run Unit Tests
```bash
./gradlew test
```

#### Run Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

#### Install & Run on Device
```bash
./gradlew installDebug          # Build and install
adb shell am start -n com.aksharadeepa.tutor/.MainActivity
```

### Adding New Features

1. **Create Data Model**
   - Add entity in `data/model/Entities.kt`
   - Create DAO in `data/dao/`
   - Run `./gradlew build` to generate Room code

2. **Create Repository**
   - Add repository in `data/repository/`
   - Implement data access logic

3. **Create ViewModel**
   - Add ViewModel in `ui/[feature]/`
   - Inject repositories via Hilt
   - Manage state with Flow

4. **Create UI Screen**
   - Add Composable in `ui/[feature]/`
   - Use ViewModel for state management
   - Apply theme colors from `ui/theme/`

5. **Add Navigation**
   - Update `NavGraph.kt` with new route
   - Link from existing screens

---

## 🐛 Troubleshooting

### AI Tips Not Showing

**Symptom**: Study tips section shows "Loading..." indefinitely

**Solutions**:
1. **Check API Key**
   - Verify `local.properties` has `ANTHROPIC_API_KEY`
   - Key format: `sk-ant-api-XXXXXXXXXXXXX`
   - Check for typos or extra whitespace

2. **Offline Mode**
   - App automatically shows offline tips if:
     - No internet connection
     - API key missing
     - API request fails
   - This is intentional and expected!

3. **API Quota**
   - Check Anthropic console for usage/limits
   - Verify API key has credits

### Database Issues

**Symptom**: App crashes or shows empty data

**Solutions**:
```bash
# Full clean and rebuild
./gradlew clean
./gradlew build

# Uninstall and reinstall
adb uninstall com.aksharadeepa.tutor
./gradlew installDebug

# Clear app data
adb shell pm clear com.aksharadeepa.tutor
```

### Quiz Timer Not Working

**Symptom**: Timer doesn't countdown or freezes

**Solutions**:
1. Check device time is accurate
2. Ensure app has all runtime permissions
3. Restart emulator/device
4. Check Logcat for exceptions

### Emulator Performance Issues

**Symptom**: App is laggy or slow

**Solutions**:
```bash
# Use hardware acceleration
# Enable HAXM (Intel) or WHPX (AMD)

# Increase emulator RAM
# Settings → System Image → Increase heap

# Use higher API level (34 is optimal)

# Close other apps to free resources

# Test on physical device (more reliable)
```

### Build Gradle Errors

**Symptom**: `./gradlew build` fails

**Solutions**:
1. **Java Version Mismatch**
   ```bash
   # Use Java 17
   java -version  # Should show 17.x.x
   ```

2. **Sync Issues**
   ```bash
   ./gradlew sync
   ./gradlew build --refresh-dependencies
   ```

3. **Kotlin Compiler**
   - Invalidate caches: File → Invalidate Caches
   - Restart Android Studio

### Network/API Issues

**Symptom**: "Network error" in logs

**Solutions**:
- Check internet connectivity
- Verify API key validity
- Check Anthropic API status
- Verify firewall/VPN settings

---

## 📈 Performance Optimization

### Database Optimization
- ✅ Foreign key indexes already configured
- ✅ Efficient Room queries with Flow
- ✅ Proper pagination for large datasets

### UI Performance
- ✅ Jetpack Compose efficient recomposition
- ✅ Lazy loading of lists
- ✅ Image optimization (using vector drawables)

### Network Optimization
- ✅ Retrofit request/response caching
- ✅ OkHttp connection pooling
- ✅ Timeout configuration (30 seconds)

### Memory Optimization
- ✅ Proper ViewModel lifecycle
- ✅ Coroutine cancellation
- ✅ No memory leaks in observers

---

## 🔒 Security Considerations

### API Key Security
```kotlin
// API key stored in local.properties (development)
// Never hardcode keys in source code
// Use BuildConfig.ANTHROPIC_API_KEY (compile-time configuration)
```

### Data Security
- ✅ Database encrypted (Room with SQLCipher optional)
- ✅ HTTPS for API communication
- ✅ No sensitive data in logs

### Permissions
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---

## 🚢 Deployment

### Pre-Release Checklist
- [ ] Test on minimum API 21 device
- [ ] Test on maximum API 34 device
- [ ] Verify all features work offline
- [ ] Test with and without API key
- [ ] Check database migrations
- [ ] Performance testing (frame rate, memory)
- [ ] Security audit (no hardcoded secrets)

### Release Steps
1. Update version in `build.gradle.kts`
2. Build release APK: `./gradlew assembleRelease`
3. Sign APK (if distribution required)
4. Test thoroughly
5. Create GitHub release
6. Upload to Play Store (if desired)

### Update Strategy
- Use Room migrations for database schema changes
- Implement version tracking in metadata
- Graceful degradation for older versions

---

## 📚 Learning Resources

### Jetpack Compose
- [Official Compose Tutorial](https://developer.android.com/jetpack/compose)
- [Compose API Reference](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary)

### Room Database
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [DAO Documentation](https://developer.android.com/training/data-storage/room/accessing-data)

### Hilt Dependency Injection
- [Hilt Guide](https://developer.android.com/training/dependency-injection/hilt-android)
- [ViewModel with Hilt](https://developer.android.com/training/dependency-injection/hilt-jetpack)

### Coroutines & Flow
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Flow Documentation](https://kotlinlang.org/docs/flow.html)

### MVVM Architecture
- [Android Architecture Guide](https://developer.android.com/jetpack/guide)
- [ViewModel Documentation](https://developer.android.com/topic/libraries/architecture/viewmodel)

### Retrofit & Networking
- [Retrofit Guide](https://square.github.io/retrofit/)
- [OkHttp Documentation](https://square.github.io/okhttp/)

---

## 🤝 Contributing

Contributions welcome! Follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Standards
- Follow Kotlin style guide
- Use meaningful variable names
- Add comments for complex logic
- Test new features
- Update this README if needed

---

## 📝 License

This project is part of an internship educational initiative. Licensed under MIT License.

---

## 👨‍💻 Author

**THARUN GOWDA K**

- GitHub: [@THARUN-GOWDA-K](https://github.com/THARUN-GOWDA-K)
- Email: Contact via repository
- Portfolio: Available on GitHub

---

## 🙏 Acknowledgments

- **Claude AI** (Anthropic): Powering study tips and AI Coach
- **Jetpack Team** (Google): Modern Android development
- **Material Design**: Design principles and components
- **Room Database**: Data persistence foundation
- **Community**: Open-source dependencies and libraries

---

## 📞 Support & Contact

### Getting Help
1. Check the **Troubleshooting** section above
2. Search existing GitHub issues
3. Review Logcat for error messages
4. Check Android Studio console output

### Reporting Issues
- Create GitHub issue with:
  - Clear title and description
  - Steps to reproduce
  - Device/emulator info
  - Logcat output
  - Screenshots if applicable

### Feature Requests
- Open discussion on GitHub
- Describe use case and benefit
- Provide mockup if UI-related

---

## 🎯 Future Roadmap

### Planned Features
- [ ] Timed practice tests (full syllabus)
- [ ] Advanced analytics dashboard
- [ ] Push notifications for reminders
- [ ] Firebase cloud sync
- [ ] Multi-language support
- [ ] Offline question download
- [ ] Custom study plans
- [ ] Peer comparison (optional)
- [ ] Adaptive difficulty
- [ ] Video tutorials integration

### Performance Improvements
- [ ] Database query optimization
- [ ] Caching strategy enhancement
- [ ] Image lazy loading
- [ ] Background sync

### UI/UX Enhancements
- [ ] Dark mode support
- [ ] Custom themes
- [ ] Gesture controls
- [ ] Voice input
- [ ] Accessibility improvements

---

## 📊 Project Statistics

| Metric | Value |
|--------|-------|
| **Language** | Kotlin |
| **Total Questions** | 225 |
| **Chapters** | 45 |
| **Subjects** | 3 |
| **Composables** | 20+ |
| **ViewModels** | 4 |
| **Database Entities** | 6 |
| **Target Audience** | 10th Grade Students |
| **Build Size** | ~15-20 MB (debug) |
| **Min SDK** | 21 |
| **Target SDK** | 34 |

---

## ✅ Validation Checklist

### Build Status
- ✅ Compiles without errors
- ✅ No critical warnings
- ✅ All dependencies resolved
- ✅ ProGuard/R8 compatible

### Feature Status
- ✅ Syllabus Tracker: Fully functional
- ✅ Quiz Flow: 5-question limit working
- ✅ Timer: 30 seconds per question
- ✅ Scoring: Accurate calculation
- ✅ AI Tips: Claude integration working
- ✅ Strength Map: Radar chart rendering
- ✅ Daily Goals: Streak tracking
- ✅ Navigation: All routes accessible
- ✅ Data Persistence: Database saving
- ✅ Offline Mode: Graceful fallback

### UI Status
- ✅ Colors: Soft Sage + Deep Olive applied
- ✅ Typography: Material 3 compliant
- ✅ Responsive: Works on multiple screen sizes
- ✅ Animations: Smooth transitions
- ✅ Accessibility: Basic support

### Testing Status
- ✅ Manual testing: All screens verified
- ✅ Emulator: Running on API 34
- ✅ Database: Prepopulation working
- ✅ Network: API fallback working
- ✅ Edge cases: Handled gracefully

---

**Last Built**: May 7, 2026  
**Status**: Production Ready ✅  
**Version**: 1.0.0

---

*Thank you for using AksharaDeepaTutor! Happy learning! 🚀*
