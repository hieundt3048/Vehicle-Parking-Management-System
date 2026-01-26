from fastapi import FastAPI, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
import cv2
import easyocr
import tempfile
import re
import os

app = FastAPI(title="Vietnam License Plate OCR PRO")

# ===== ADD CORS MIDDLEWARE =====
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],  # Your frontend URL
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ===== LOAD OCR =====
reader = easyocr.Reader(['en'], gpu=False)

# ===== NORMALIZE OCR TEXT =====
def normalize_text(text: str) -> str:
    text = text.upper()
    replace_map = {
        "O": "0",
        "I": "1",
        "Z": "2",
        "S": "5",
        "B": "8"
    }
    for k, v in replace_map.items():
        text = text.replace(k, v)
    return text

# ===== MERGE PLATE (PRO MAX) =====
def merge_plate_pro(texts: list[str]) -> str | None:
    if not texts:
        return None

    joined = " ".join(texts)
    joined = normalize_text(joined)

    # Giữ lại chữ, số, dấu . -
    joined = re.sub(r"[^A-Z0-9.\- ]", " ", joined)
    joined = re.sub(r"\s+", " ", joined).strip()

    # ===== 1️⃣ BIỂN MỚI: 99H77060 =====
    new_plate = re.search(r"(\d{2})\s*[-]?\s*([A-Z]\d)\s*(\d{4,5})", joined)
    if new_plate:
        return f"{new_plate.group(1)}{new_plate.group(2)}{new_plate.group(3)}"

    # ===== 2️⃣ BIỂN CŨ: 86A.099.99 =====
    old_plate = re.search(
        r"(\d{2})\s*([A-Z])[\.\-]?\s*(\d{3})[\.\-]?\s*(\d{2})",
        joined
    )
    if old_plate:
        return f"{old_plate.group(1)}{old_plate.group(2)}{old_plate.group(3)}{old_plate.group(4)}"

    return None

# ===== API =====
@app.post("/detect-plate")
async def detect_plate(file: UploadFile = File(...)):
    with tempfile.NamedTemporaryFile(delete=False, suffix=".jpg") as tmp:
        tmp.write(await file.read())
        image_path = tmp.name

    img = cv2.imread(image_path)
    if img is None:
        os.remove(image_path)
        return {"error": "Không đọc được ảnh"}

    ocr_results = reader.readtext(img, detail=0, paragraph=True)

    raw_texts = [t.strip() for t in ocr_results if len(t.strip()) >= 2]

    plate = merge_plate_pro(raw_texts)

    os.remove(image_path)

    return {
        "plate": plate,
        "raw_results": raw_texts,
        "count": len(raw_texts)
    }

# ===== ROOT =====
@app.get("/")
def root():
    return {"status": "License Plate OCR PRO running"}