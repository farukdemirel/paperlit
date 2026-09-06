# PaperLit Eğitim Rehberi

> **Bağlayıcı ürün kapsamı:** Geliştirmeye başlamadan önce [PaperLit — Kanonik Ürün Tanımı](../PRODUCT_DEFINITION.md) okunmalıdır. PaperLit bir PDF, e-kitap veya belge okuyucu değildir.

Bu klasör, PaperLit Android uygulamasını geliştirirken izlenen öğretici sürecin kalıcı kaydıdır.

PaperLit; ürün ambalajındaki miktar bilgilerini kamera/OCR ile okumayı, kullanıcıya doğrulatmayı, fiyat ve birim maliyet karşılaştırmaları yapmayı ve her doğrulanmış okumayı yerel SQLite veritabanında saklamayı hedefler.

Amaç yalnızca çalışan bir uygulama üretmek değildir. Her geliştirme adımında kullanılan Android, Git ve test kavramlarının öğrenilmesi; alınan kararların nedenleriyle birlikte saklanması hedeflenir.

## Nasıl kullanılmalı?

Bölümler numara sırasıyla okunmalıdır. Her bölüm mümkün olduğunca şu soruları cevaplar:

1. Ne yapıyoruz?
2. Neden yapıyoruz?
3. Hangi kavramları öğreniyoruz?
4. Kodda ne değişti?
5. Sonucu nasıl doğrularız?
6. Bölümün tamamlandığını nasıl anlarız?

## Bölümler

1. [Projeye Başlangıç ve Çalışma Yöntemi](01-projeye-baslangic-ve-calisma-yontemi.md)
2. [Android Teknoloji Seçimi ve Proje Yapısı](02-android-teknoloji-secimi-ve-proje-yapisi.md)
3. [İlk Android Proje İskeleti](03-ilk-android-proje-iskelesi.md)
4. [Telefonsuz Test: GitHub Üzerinde Sanal Android Cihazı](04-telefonsuz-test-sanal-android-cihazi.md)

> 03 ve 04. bölümlerde geçen `LibraryUiState` ve “Kütüphanem boş” ekranı gerçek ürün tasarımı değildir. Yanlış ürün varsayımıyla oluşturulmuş, yalnız Android/Compose/CI hattını doğrulayan geçici iskelettir.

Yeni geliştirme aşamaları başladıkça bu listeye yeni bölümler eklenecektir.

## Temel proje kuralları

- Uygulama telefonda yerel çalışır.
- Kullanıcı verileri cihazdaki SQLite veritabanında tutulur.
- Uygulamanın temel çalışması için hosting veya internet gerekmez.
- OCR sonucu kullanıcı onayı olmadan kesin veri kabul edilmez.
- Marka okunamazsa elle girilebilir veya geçmiş veriden seçilebilir.
- Fiyat elle girilebilir; ayrı fiyat etiketi okuması daha sonra eklenebilir.
- APK, GitHub Actions üzerinden üretilebilir.
- `main` yalnızca kabul edilmiş ve çalışan sürümleri taşır.
- Geliştirmeler `feature/...` dallarında yapılır.
- Değişiklikler test edilir ve Pull Request üzerinden incelenir.
