# PaperLit

PaperLit, Android telefonda çalışan; ürün ambalajındaki miktar bilgilerini kamera ve OCR yardımıyla okuyarak fiyat karşılaştırması yapan yerel bir uygulamadır.

Uygulamanın temel akışı:

1. Ürün ambalajını kamerayla oku.
2. Rulo/yaprak sayısı, kat sayısı, ölçü ve uzunluk gibi bilgileri çıkar.
3. Eksik veya hatalı alanları kullanıcıya doğrulat.
4. Fiyatı elle al veya ayrı bir fiyat etiketi okumasıyla elde et.
5. Şeffaf birim maliyet ve fiyat/performans değerlerini hesapla.
6. Her doğrulanmış okumayı yerel SQLite veritabanında sakla.
7. Daha önce görülen ürünleri yeniden kullan, karşılaştır veya güncelle.

Uygulama çevrimdışı ve hosting zorunluluğu olmadan çalışacaktır.

> **Kapsam uyarısı:** PaperLit bir PDF, e-kitap veya belge okuyucu değildir. Branch'teki erken `LibraryUiState` ekranı yalnız Android/Compose/CI hattını kanıtlamak için oluşturulmuş geçici ve yanlış adlandırılmış bir teknik iskelettir; ürün tasarımı değildir.

Projenin bağlayıcı ürün tanımı için [docs/PRODUCT_DEFINITION.md](docs/PRODUCT_DEFINITION.md) belgesine bakın.

Android geliştirme eğitim günlüğü: [docs/tutorial/](docs/tutorial/)
