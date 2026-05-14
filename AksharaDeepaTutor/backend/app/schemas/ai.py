from typing import List, Optional
from pydantic import BaseModel


class AiTipRequest(BaseModel):
    prompt: str
    user_id: Optional[str] = None
    topic: Optional[str] = None


class AiTipResponse(BaseModel):
    tips: List[str]
    fallback: bool = False
