from typing import Dict, List

from pydantic import BaseModel


class ProgressUpdateRequest(BaseModel):
    user_id: str
    subject: str
    completed_chapters: int
    total_chapters: int
    updated_at: int
    completed_chapter_ids: List[int] = []
    completion_dates: Dict[str, str] = {}


class ProgressResponse(BaseModel):
    user_id: str
    subject: str
    completed_chapters: int
    total_chapters: int
    updated_at: int
    completed_chapter_ids: List[int] = []
    completion_dates: Dict[str, str] = {}
