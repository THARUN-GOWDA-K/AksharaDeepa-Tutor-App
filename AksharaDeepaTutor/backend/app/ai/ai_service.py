from typing import List

import httpx

from app.utils.config import get_settings


async def generate_tips(prompt: str) -> List[str]:
    settings = get_settings()

    if not settings.ai_api_key:
        return _offline_tips()

    provider = settings.ai_provider.lower().strip()
    if provider == "claude":
        return await _call_claude(prompt, settings.ai_api_key)

    if provider == "gemini":
        return await _call_gemini(prompt, settings.ai_api_key)

    return _offline_tips()


async def _call_gemini(prompt: str, api_key: str) -> List[str]:
    url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent"
    payload = {
        "contents": [{"parts": [{"text": prompt}]}],
        "generationConfig": {
            "temperature": 0.5,
            "maxOutputTokens": 220
        }
    }

    try:
        async with httpx.AsyncClient(timeout=12.0) as client:
            response = await client.post(url, params={"key": api_key}, json=payload)
            response.raise_for_status()
            data = response.json()
            text = (
                data.get("candidates", [{}])[0]
                .get("content", {})
                .get("parts", [{}])[0]
                .get("text", "")
            )
            return _split_tips(text)
    except Exception:
        return _offline_tips()


async def _call_claude(prompt: str, api_key: str) -> List[str]:
    url = "https://api.anthropic.com/v1/messages"
    headers = {
        "x-api-key": api_key,
        "anthropic-version": "2023-06-01",
        "content-type": "application/json"
    }
    payload = {
        "model": "claude-3-haiku-20240307",
        "max_tokens": 220,
        "temperature": 0.6,
        "messages": [{"role": "user", "content": prompt}]
    }

    try:
        async with httpx.AsyncClient(timeout=12.0) as client:
            response = await client.post(url, headers=headers, json=payload)
            response.raise_for_status()
            data = response.json()
            text = data.get("content", [{}])[0].get("text", "")
            return _split_tips(text)
    except Exception:
        return _offline_tips()


def _split_tips(text: str) -> List[str]:
    cleaned = [line.strip("-• ") for line in text.split("\n") if line.strip()]
    if not cleaned:
        return _offline_tips()
    return cleaned[:3]


def _offline_tips() -> List[str]:
    return [
        "Revise key concepts before retrying the quiz.",
        "Practice two weak topics and explain them in your own words.",
        "Use short daily sessions instead of long cramming blocks."
    ]
