from typing import Optional
from pydantic import BaseModel, EmailStr


class ProfileResponse(BaseModel):
    user_id: str
    display_name: str
    email: EmailStr
    photo_url: Optional[str] = None
    grade: str = "10"
    school: Optional[str] = None
    streak_count: int = 0


class ProfileUpdateRequest(BaseModel):
    display_name: Optional[str] = None
    photo_url: Optional[str] = None
    school: Optional[str] = None
    grade: Optional[str] = None
