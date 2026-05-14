import asyncio
from fastapi import APIRouter, HTTPException

from app.firebase.firebase_admin import get_firestore_client, is_firebase_ready
from app.schemas.profile import ProfileResponse, ProfileUpdateRequest

router = APIRouter()


@router.get("/profile", response_model=ProfileResponse)
async def get_profile(user_id: str) -> ProfileResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    def _fetch() -> ProfileResponse:
        db = get_firestore_client()
        doc = db.collection("users").document(user_id).get()
        data = doc.to_dict() or {}
        return ProfileResponse(
            user_id=user_id,
            display_name=str(data.get("display_name", "")),
            email=str(data.get("email", "")),
            photo_url=data.get("photo_url"),
            grade=str(data.get("grade", "10")),
            school=data.get("school"),
            streak_count=int(data.get("streak_count", 0))
        )

    return await asyncio.to_thread(_fetch)


@router.put("/profile", response_model=ProfileResponse)
async def update_profile(user_id: str, payload: ProfileUpdateRequest) -> ProfileResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    def _save() -> ProfileResponse:
        db = get_firestore_client()
        ref = db.collection("users").document(user_id)
        existing = ref.get().to_dict() or {}
        updated = {**existing, **payload.model_dump(exclude_none=True)}
        ref.set(updated)
        return ProfileResponse(
            user_id=user_id,
            display_name=str(updated.get("display_name", "")),
            email=str(updated.get("email", "")),
            photo_url=updated.get("photo_url"),
            grade=str(updated.get("grade", "10")),
            school=updated.get("school"),
            streak_count=int(updated.get("streak_count", 0))
        )

    return await asyncio.to_thread(_save)
