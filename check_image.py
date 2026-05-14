import os
from PIL import Image

image_path = r"C:\Users\tharu\.gemini\antigravity\brain\ac768016-bf33-4664-a55e-fa4988f80b2c\media__1778768300487.jpg"
if os.path.exists(image_path):
    with Image.open(image_path) as img:
        print(f"Image 1: {img.format}, {img.size}")

image2_path = r"C:\Users\tharu\.gemini\antigravity\brain\ac768016-bf33-4664-a55e-fa4988f80b2c\media__1778768362468.png"
if os.path.exists(image2_path):
    with Image.open(image2_path) as img:
        print(f"Image 2: {img.format}, {img.size}")
