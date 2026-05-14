from fastapi import APIRouter, HTTPException
from firebase_admin import auth

from app.firebase.firebase_admin import is_firebase_ready
from app.schemas.auth import AuthResponse, ForgotPasswordRequest, LoginRequest, SignupRequest
from app.schemas.common import ApiResponse

router = APIRouter()


@router.post("/signup", response_model=AuthResponse)
async def signup(payload: SignupRequest) -> AuthResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    try:
        user = auth.create_user(
            email=payload.email,
            password=payload.password,
            display_name=payload.display_name
        )
        return AuthResponse(uid=user.uid, email=user.email, display_name=user.display_name or "")
    except auth.EmailAlreadyExistsError:
        raise HTTPException(status_code=409, detail="Email is already registered.")


@router.post("/login", response_model=AuthResponse)
async def login(payload: LoginRequest) -> AuthResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    if not payload.id_token:
        raise HTTPException(status_code=400, detail="id_token is required.")

    try:
        decoded = auth.verify_id_token(payload.id_token)
        user = auth.get_user(decoded["uid"])
        return AuthResponse(uid=user.uid, email=user.email or "", display_name=user.display_name or "")
    except Exception:
        raise HTTPException(status_code=401, detail="Invalid token.")


@router.post("/forgot-password", response_model=ApiResponse)
async def forgot_password(payload: ForgotPasswordRequest) -> ApiResponse:
    if not is_firebase_ready():
        raise HTTPException(status_code=503, detail="Firebase not configured.")

    try:
        auth.generate_password_reset_link(payload.email)
        return ApiResponse(success=True, message="Password reset email sent.")
    except Exception:
        raise HTTPException(status_code=404, detail="Email not found.")
