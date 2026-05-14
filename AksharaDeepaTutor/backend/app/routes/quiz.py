import asyncio
from typing import List

from fastapi import APIRouter, HTTPException, Query

from app.firebase.firebase_admin import get_firestore_client, is_firebase_ready
from app.schemas.quiz import QuizHistoryResponse, QuizQuestion, QuizSubmitRequest, QuizSubmitResponse

router = APIRouter()


@router.get("/questions", response_model=List[QuizQuestion])
async def get_questions(chapter_id: str = Query(...)) -> List[QuizQuestion]:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    def _fetch() -> List[QuizQuestion]:
        db = get_firestore_client()
        docs = db.collection("questions").where("chapterId", "==", chapter_id).stream()
        items: List[QuizQuestion] = []
        for doc in docs:
            data = doc.to_dict() or {}
            items.append(
                QuizQuestion(
                    id=doc.id,
                    chapter_id=str(data.get("chapterId", "")),
                    subject=str(data.get("subject", "")),
                    question_text=str(data.get("questionText", "")),
                    option_a=str(data.get("optionA", "")),
                    option_b=str(data.get("optionB", "")),
                    option_c=str(data.get("optionC", "")),
                    option_d=str(data.get("optionD", "")),
                    correct_option=str(data.get("correctOption", "")),
                    explanation=str(data.get("explanation", "")),
                    difficulty=str(data.get("difficulty", "medium"))
                )
            )
        return items

    return await asyncio.to_thread(_fetch)


@router.post("/submit-quiz", response_model=QuizSubmitResponse)
async def submit_quiz(payload: QuizSubmitRequest) -> QuizSubmitResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    def _save() -> QuizSubmitResponse:
        db = get_firestore_client()
        attempt_ref = db.collection("users").document(payload.user_id).collection("quiz_attempts").document()
        attempt_ref.set(payload.model_dump())
        return QuizSubmitResponse(
            attempt_id=attempt_ref.id,
            score=payload.score,
            total_questions=payload.total_questions
        )

    return await asyncio.to_thread(_save)


@router.get("/quiz-history", response_model=QuizHistoryResponse)
async def quiz_history(user_id: str = Query(...)) -> QuizHistoryResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    def _fetch() -> QuizHistoryResponse:
        db = get_firestore_client()
        attempts = db.collection("users").document(user_id).collection("quiz_attempts").stream()
        items = []
        for doc in attempts:
            data = doc.to_dict() or {}
            items.append(
                {
                    "attempt_id": doc.id,
                    "chapter_id": str(data.get("chapter_id", data.get("chapterId", ""))),
                    "score": int(data.get("score", 0)),
                    "total_questions": int(data.get("total_questions", data.get("totalQuestions", 0))),
                    "attempted_at": int(data.get("attempted_at", data.get("attemptedAt", 0)))
                }
            )
        return QuizHistoryResponse(user_id=user_id, items=items)

    return await asyncio.to_thread(_fetch)
