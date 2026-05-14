import json
from typing import Optional

import firebase_admin
from firebase_admin import credentials, firestore

from app.utils.config import get_settings

_app: Optional[firebase_admin.App] = None


def init_firebase() -> Optional[firebase_admin.App]:
    global _app
    if _app is not None:
        return _app

    settings = get_settings()

    if settings.firebase_service_account_json:
        service_info = json.loads(settings.firebase_service_account_json)
        if "private_key" in service_info:
            service_info["private_key"] = service_info["private_key"].replace("\\n", "\n")
        cred = credentials.Certificate(service_info)
        _app = firebase_admin.initialize_app(cred)
        return _app

    if settings.firebase_credentials_path:
        cred = credentials.Certificate(settings.firebase_credentials_path)
        _app = firebase_admin.initialize_app(cred)
        return _app

    return None


def is_firebase_ready() -> bool:
    return _app is not None


def get_firestore_client() -> firestore.Client:
    if _app is None:
        raise RuntimeError("Firebase is not initialized.")
    return firestore.client(_app)
