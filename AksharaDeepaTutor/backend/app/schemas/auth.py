from pydantic import BaseModel, EmailStr, Field


class SignupRequest(BaseModel):
    email: EmailStr
    password: str = Field(min_length=6)
    display_name: str = Field(min_length=2, max_length=60)


class LoginRequest(BaseModel):
    id_token: str = Field(default="", description="Firebase ID token from client auth")


class ForgotPasswordRequest(BaseModel):
    email: EmailStr


class AuthResponse(BaseModel):
    uid: str
    email: EmailStr
    display_name: str = ""
