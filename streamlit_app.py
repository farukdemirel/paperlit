import json
import sqlite3
from datetime import datetime
from google import genai
from PIL import Image
import pandas as pd
import streamlit as st
from streamlit_js_eval import get_geolocation

st.set_page_config(
    page_title="🧻 Market F/P & Fiyat Takipçisi", layout="centered"
)

# =============================================================================
# VERİTABANI YÖNETİMİ (SQLite)
# =============================================================================
DB_FILE = "tp_history.db"

def init_db():
    conn = sqlite3.connect(DB_FILE)
    c = conn.cursor()
    c.execute("""
        CREATE TABLE IF NOT EXISTS scans (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp TEXT,
            magaza TEXT,
            marka TEXT,
            fiyat REAL,
            rulo INTEGER,
            kat INTEGER,
            yaprak INTEGER,
            fp_skoru REAL,
            konum TEXT
        )
    """)
    conn.commit()
    conn.close()

init_db()

# =============================================================================
# UYGULAMA ARAYÜZÜ
# =============================================================================
st.title("🧻 Market F/P & Fiyat Takipçisi")

api_key = (
    st.secrets.get("GEMINI_API_KEY")
    if "GEMINI_API_KEY" in st.secrets
    else st.sidebar.text_input("Gemini API Key", type="password")
)

# --- GÜVENLİ KONUM ALMA ---
location = get_geolocation()
coords = "Konum Alınamadı"
if (
    location
    and isinstance(location, dict)
    and "coords" in location
    and location["coords"]
):
    try:
        lat = location["coords"].get("latitude")
        lon = location["coords"].get("longitude")
        if lat and lon:
            coords = f"{lat:.4f}, {lon:.4f}"
    except Exception:
        coords = "Konum Alınamadı"

tab1, tab2 = st.tabs(["📸 Yeni Ürün Tara", "📊 Geçmiş & Nerede Ucuz?"])

# --- TAB 1: TARAMA VE HESAPLAMA ---
with tab1:
    st.write("Kamerayı ambalaja ve (varsa) raf etiketine gösterecek şekilde çekin.")
    
    # st.form kullanımı mobilde Enter tuşu zorunluluğunu kaldırır
    with st.form("urun_giris_formu"):
        img_file = st.camera_input("Fotoğraf Çek")

        manuel_fiyat = st.number_input(
            "Etiketteki Fiyat (Görselden okuyamazsa buraya girin)",
            min_value=0.0,
            value=0.0,
            step=1.0,
        )
        magaza_input = st.text_input(
            "Mağaza Adı (Örn: Migros, A101 - Boşsa görselden okunur)"
        )
        
        # Ekrandaki tetikleyici buton
        submit_btn = st.form_submit_button("🔍 Ürünü Analiz Et / Hesapla", use_container_width=True)

    if submit_btn:
        if not api_key:
            st.error("Lütfen Gemini API anahtarınızı girin.")
        elif not img_file:
            st.error("Lütfen önce bir fotoğraf çekin.")
        else:
            client = genai.Client(api_key=api_key)
            image = Image.open(img_file)

            prompt = """
            Bu görseldeki tuvalet kağıdı ambalajını ve raf etiketini analiz et. SADECE aşağıdaki JSON formatında yanıt ver:
            {
              "marka": "Marka adı",
              "magaza": "Etiketten tespit edilen mağaza/market adı (yoksa null)",
              "fiyat": Float (Etiketten okunan fiyat TL cinsinden, yoksa null),
              "rulo_sayisi": Integer (yoksa null),
              "kat_sayisi": Integer (yoksa null),
              "yaprak_sayisi": Integer (rulo başına yaprak, yoksa null)
            }
            """

            with st.spinner("Görsel ve etiket analiz ediliyor..."):
                try:
                   # YENİ KOD (Güncel)
                    response = client.models.generate_content(
                        model="gemini-1.5-flash", contents=[image, prompt]
                    )

                    raw_text = (
                        response.text.replace("```json", "")
                        .replace("```", "")
                        .strip()
                    )
                    data = json.loads(raw_text)

                    marka = data.get("marka") or "Bilinmeyen Marka"
                    magaza = magaza_input or data.get("magaza") or "Bilinmeyen Market"
                    fiyat = (
                        manuel_fiyat if manuel_fiyat > 0 else (data.get("fiyat") or 0.0)
                    )

                    rulo = data.get("rulo_sayisi") or 1
                    kat = data.get("kat_sayisi") or 1
                    yaprak = data.get("yaprak_sayisi") or 0
                    toplam_yaprak = rulo * yaprak

                    st.subheader(f"🔍 {marka} @ {magaza}")

                    if fiyat <= 0:
                        st.warning(
                            "⚠️ Fiyat görselden okunamadı. Lütfen yukarıdaki alana fiyat girip butona tekrar basın."
                        )
                    else:
                        rulo_basi_fiyat = fiyat / rulo
                        gercek_fp_skoru = (
                            (fiyat / (toplam_yaprak * kat)) * 100
                            if (toplam_yaprak * kat) > 0
                            else 0
                        )

                        st.write(f"• **Fiyat:** {fiyat:.2f} TL")
                        st.write(f"• **Rulo / Kat:** {rulo} Rulo / {kat} Kat")
                        st.write(f"• **Rulo Başı Yaprak:** {yaprak} Yaprak")

                        st.markdown("---")
                        st.metric("Rulo Başı Fiyat", f"{rulo_basi_fiyat:.2f} TL")
                        st.info(
                            f"💡 **Kat Ayarlı F/P Skoru:** {gercek_fp_skoru:.3f}\n\n*(Skor ne kadar DÜŞÜKSE ürün o kadar ucuzdur)*"
                        )

                        conn = sqlite3.connect(DB_FILE)
                        c = conn.cursor()
                        ts = datetime.now().strftime("%Y-%m-%d %H:%M")
                        c.execute(
                            """
                            INSERT INTO scans (timestamp, magaza, marka, fiyat, rulo, kat, yaprak, fp_skoru, konum)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """,
                            (
                                ts,
                                magaza,
                                marka,
                                fiyat,
                                rulo,
                                kat,
                                yaprak,
                                gercek_fp_skoru,
                                coords,
                            ),
                        )
                        conn.commit()
                        conn.close()
                        st.success("Veri başarıyla kaydedildi!")

                except Exception as e:
                    st.error(f"Hata oluştu: {e}")

# --- TAB 2: GEÇMİŞ VE KARŞILAŞTIRMA ---
with tab2:
    st.subheader("📋 Kayıtlı Ürünler ve F/P Karşılaştırması")
    conn = sqlite3.connect(DB_FILE)
    c = conn.cursor()
    c.execute(
        "SELECT timestamp, magaza, marka, fiyat, rulo, kat, fp_skoru, konum FROM scans ORDER BY fp_skoru ASC"
    )
    rows = c.fetchall()
    conn.close()

    if rows:
        st.write("🏆 **En Ucuzdan (En İyi F/P) En Pahalıya Sıralama:**")

        df = pd.DataFrame(
            rows,
            columns=[
                "Tarih",
                "Mağaza",
                "Marka",
                "Fiyat (TL)",
                "Rulo",
                "Kat",
                "F/P Skoru",
                "Konum",
            ],
        )
        st.dataframe(df, use_container_width=True)

        if st.button("🗑️ Geçmişi Temizle"):
            conn = sqlite3.connect(DB_FILE)
            conn.cursor().execute("DELETE FROM scans")
            conn.commit()
            conn.close()
            st.rerun()
    else:
        st.info("Henüz kayıtlı bir veri yok. Ürün tarayıp kaydedin.")
