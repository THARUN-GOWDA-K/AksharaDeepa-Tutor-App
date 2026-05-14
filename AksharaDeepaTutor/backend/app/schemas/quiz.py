from typing import List, Optional
from pydantic import BaseModel


class QuizQuestion(BaseModel):
    id: str
    chapter_id: str
    subject: str
    question_text: str
    option_a: str
    option_b: str
    option_c: str
    option_d: str
    correct_option: str
    explanation: str
    difficulty: str = "medium"


class QuizAnswer(BaseModel):
    question_id: str
    selected_option: str
    is_correct: bool


class QuizSubmitRequest(BaseModel):
    user_id: str
    chapter_id: str
    score: int
    total_questions: int
    answers: List[QuizAnswer]
    attempted_at: int


class QuizSubmitResponse(BaseModel):
    attempt_id: str
    score: int
    total_questions: int


class QuizHistoryItem(BaseModel):
    attempt_id: str
    chapter_id: str
    score: int
    total_questions: int
    attempted_at: int


class QuizHistoryResponse(BaseModel):
    user_id: str
    items: List[QuizHistoryItem]
