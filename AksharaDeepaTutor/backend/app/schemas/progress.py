from pydantic import BaseModel


class ProgressUpdateRequest(BaseModel):
    user_id: str
    subject: str
    completed_chapters: int
    total_chapters: int
    updated_at: int


class ProgressResponse(BaseModel):
    user_id: str
    subject: str
    completed_chapters: int
    total_chapters: int
    updated_at: int
