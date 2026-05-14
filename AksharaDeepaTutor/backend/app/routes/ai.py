from fastapi import APIRouter

from app.ai.ai_service import generate_tips
from app.schemas.ai import AiTipRequest, AiTipResponse

router = APIRouter()


@router.post("/ai-study-tip", response_model=AiTipResponse)
async def ai_study_tip(payload: AiTipRequest) -> AiTipResponse:
    tips = await generate_tips(payload.prompt)
    fallback = False
    if tips and tips[0].startswith("Revise key concepts"):
        fallback = True
    return AiTipResponse(tips=tips, fallback=fallback)
