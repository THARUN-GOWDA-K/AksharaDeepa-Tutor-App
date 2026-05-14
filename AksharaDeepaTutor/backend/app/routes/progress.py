import asyncio
from fastapi import APIRouter, HTTPException

from app.firebase.firebase_admin import get_firestore_client, is_firebase_ready
from app.schemas.progress import ProgressResponse, ProgressUpdateRequest

router = APIRouter()


@router.get("/progress", response_model=ProgressResponse)
async def get_progress(user_id: str, subject: str) -> ProgressResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    def _fetch() -> ProgressResponse:
        db = get_firestore_client()
        doc = db.collection("users").document(user_id).collection("progress").document(subject).get()
        data = doc.to_dict() or {}
        return ProgressResponse(
            user_id=user_id,
            subject=subject,
            completed_chapters=int(data.get("completed_chapters", 0)),
            total_chapters=int(data.get("total_chapters", 0)),
            updated_at=int(data.get("updated_at", 0))
        )

    return await asyncio.to_thread(_fetch)


@router.post("/update-progress", response_model=ProgressResponse)
async def update_progress(payload: ProgressUpdateRequest) -> ProgressResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    def _save() -> ProgressResponse:
        db = get_firestore_client()
        db.collection("users").document(payload.user_id).collection("progress").document(payload.subject).set(
            payload.model_dump()
        )
        return ProgressResponse(**payload.model_dump())

    return await asyncio.to_thread(_save)
