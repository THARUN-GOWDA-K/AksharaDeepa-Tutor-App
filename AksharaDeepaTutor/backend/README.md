# Akshara Deepa Tutor Backend

FastAPI backend that powers authentication validation, quiz history, progress sync, profile updates, and AI study tips.

## Setup

```bash
cd backend
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
```

## Environment

Copy `.env.example` to `.env` and fill the values:

- `FIREBASE_SERVICE_ACCOUNT_JSON` (preferred) or `FIREBASE_CREDENTIALS_PATH`
- `AI_PROVIDER` (`gemini` or `claude`)
- `AI_API_KEY`

## Run

```bash
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

## API Endpoints

- `POST /signup`
- `POST /login`
- `POST /forgot-password`
- `GET /questions?chapter_id=...`
- `POST /submit-quiz`
- `GET /quiz-history?user_id=...`
- `GET /progress?user_id=...&subject=...`
- `POST /update-progress`
- `POST /ai-study-tip`
- `GET /profile?user_id=...`
- `PUT /profile?user_id=...`

## Notes

- Client-side Firebase Auth should generate an `id_token` that is sent to `POST /login`.
- Firestore is used for cloud sync. Room remains the offline cache.
