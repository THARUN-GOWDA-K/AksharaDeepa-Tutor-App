from functools import lru_cache
from typing import List

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    app_name: str = "Akshara Deepa Tutor API"
    environment: str = "dev"
    allowed_origins: List[str] = ["*"]

    firebase_service_account_json: str = ""
    firebase_credentials_path: str = ""

    ai_provider: str = "gemini"
    ai_api_key: str = ""

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        case_sensitive=False
    )


@lru_cache
def get_settings() -> Settings:
    return Settings()
