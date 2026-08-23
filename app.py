import streamlit as st
from google import genai
from PIL import Image
import json

st.set_page_config(page_title="Tuvalet Kağıdı F/P Dedektörü", layout="centered")

st.title("🧻 Tuvalet Kağıdı F/P Analizörü")

# API Key Yönetimi (Secrets veya Manuel Giriş)
api_key = st.secrets.get("GEMINI_API_KEY") if "GEMINI_API_KEY" in st.secrets else st.sidebar.text_input("Gemini API Key", type="password")

fiyat = st.number_input("Paket Fiyatı (TL)", min_value=0.0, step=1.0, format="%.2f")
img_file = st.camera_input("Ambalaj Fotoğrafı Çek")

if img_file and fiyat > 0 and api_key:
    client = genai.Client(api_key=api_key)
    image = Image.open(img_file)
    
    prompt = """
    Bu tuvalet kağıdı ambalajı fotoğrafından şu bilgileri çıkar ve SADECE geçerli bir JSON formatında döndür:
    {
      "marka": "Marka adı",
      "rulo_sayisi": Integer (yoksa null),
      "kat_sayisi": Integer (yoksa null),
      "yaprak_sayisi": Integer (rulo başına yaprak, yoksa null),
      "toplam_uzunluk_m": Float (toplam metre, yoksa null)
    }
    Eğer ambalajda doğrudan yazmıyorsa tahmin etme, null ver.
    """

    with st.spinner("Ambalaj analiz ediliyor..."):
        try:
            response = client.models.generate_content(
                model="gemini-2.5-flash",
                contents=[image, prompt]
            )
            
            raw_text = response.text.replace("```json", "").replace("```", "").strip()
            data = json.loads(raw_text)
            
            st.subheader(f"🔍 Tespit Edilen: {data.get('marka', 'Bilinmeyen Marka')}")
            
            rulo = data.get("rulo_sayisi") or 1
            kat = data.get("kat_sayisi") or 1
            yaprak = data.get("yaprak_sayisi") or 0
            toplam_yaprak = rulo * yaprak
            
            st.write(f"• **Rulo Sayısı:** {rulo} Adet")
            st.write(f"• **Kat Sayısı:** {kat} Kat")
            st.write(f"• **Rulo Başı Yaprak:** {yaprak} Yaprak")
            
            rulo_basi_fiyat = fiyat / rulo
            
            st.markdown("---")
            st.markdown("### 📊 F/P Metrikleri")
            
            st.metric("Rulo Başı Fiyat", f"{rulo_basi_fiyat:.2f} TL")
            
            if toplam_yaprak > 0:
                yüz_yaprak_fiyat = (fiyat / toplam_yaprak) * 100
                st.metric("100 Yaprak Maliyeti", f"{yüz_yaprak_fiyat:.2f} TL")
                
                gercek_fp_skoru = (fiyat / (toplam_yaprak * kat)) * 100
                st.info(f"💡 **Kat Ayarlı F/P Skoru:** {gercek_fp_skoru:.3f}\n\n*(Bu skor ne kadar DÜŞÜKSE, ödediğiniz paraya karşılık aldığınız selüloz/kağıt miktarı o kadar YÜKSEKTİR)*")

        except Exception as e:
            st.error(f"Hata oluştu veya ambalaj tam okunamadı: {e}")
