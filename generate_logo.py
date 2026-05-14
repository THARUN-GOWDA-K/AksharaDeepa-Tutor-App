import os
from PIL import Image

image_path = r"C:\Users\tharu\.gemini\antigravity\brain\ac768016-bf33-4664-a55e-fa4988f80b2c\media__1778768300487.jpg"
base_dir = r"c:\Users\tharu\OneDrive\Desktop\Internship Project\AksharaDeepaTutor\app\src\main\res"

sizes = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

if not os.path.exists(image_path):
    print("Logo not found!")
    exit(1)

with Image.open(image_path) as img:
    # ensure RGBA for png
    if img.mode != 'RGBA':
        img = img.convert('RGBA')
        
    for folder, size in sizes.items():
        folder_path = os.path.join(base_dir, folder)
        os.makedirs(folder_path, exist_ok=True)
        
        resized_img = img.resize((size, size), Image.Resampling.LANCZOS)
        
        # save ic_launcher.png
        resized_img.save(os.path.join(folder_path, "ic_launcher.png"), format="PNG")
        # save ic_launcher_round.png
        resized_img.save(os.path.join(folder_path, "ic_launcher_round.png"), format="PNG")
        
        print(f"Saved {size}x{size} to {folder}")

print("Logo generation complete.")
