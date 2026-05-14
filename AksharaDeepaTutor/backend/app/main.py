from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.routes import ai, auth, profile, progress, quiz
from app.firebase.firebase_admin import init_firebase
from app.utils.config import get_settings


def create_app() -> FastAPI:
    settings = get_settings()

    app = FastAPI(
        title="Akshara Deepa Tutor API",
        version="1.0.0",
        description="FastAPI backend for Akshara Deepa Tutor."
    )

    app.add_middleware(
        CORSMiddleware,
        allow_origins=settings.allowed_origins,
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    app.include_router(auth.router)
    app.include_router(quiz.router)
    app.include_router(progress.router)
    app.include_router(ai.router)
    app.include_router(profile.router)

    @app.on_event("startup")
    def on_startup() -> None:
        init_firebase()

    @app.get("/")
    def root() -> dict:
        return {"status": "ok", "service": settings.app_name}

    return app


app = create_app()
