# 01 — Projeye Başlangıç ve Çalışma Yöntemi

## 1. Projenin amacı

PaperLit, Android telefonda çalışan; ürün ambalajındaki miktar bilgilerini kamera ve OCR yardımıyla okuyarak fiyat karşılaştırması yapan yerel bir uygulamadır.

Uygulama, OCR ile bulunan rulo/adet sayısı, yaprak sayısı, kat sayısı, ölçü ve uzunluk gibi bilgileri kullanıcıya doğrulatır. Marka okunamazsa kullanıcı elle yazabilir veya geçmiş markalardan seçebilir. Fiyat elle girilebilir; ileride ayrı bir raf/fiyat etiketi fotoğrafından da okunabilir.

Doğrulanmış her okuma cihazdaki SQLite veritabanına kaydedilir. Daha önce görülen ürünler yeniden bulunabilir, karşılaştırılabilir ve güncellenebilir. Temel kullanım internet veya hosting gerektirmez.

Bağlayıcı ve ayrıntılı kapsam: [PaperLit — Kanonik Ürün Tanımı](../PRODUCT_DEFINITION.md)

**PaperLit bir PDF, e-kitap, belge yönetimi veya belge okuma uygulaması değildir.**

Bu proje aceleyle tamamlanacak tek seferlik bir kod üretimi değildir. Aynı zamanda uygulamalı bir Android geliştirme eğitimidir.

Başlangıçta bilgisayarda Android geliştirme ve derleme ortamının bulunması zorunlu değildir. APK'lar GitHub Actions kullanılarak üretilebilir; ekran akışları sanal Android cihazında test edilebilir. Gerçek kamera ve market kullanımı zamanı geldiğinde fiziksel telefonla doğrulanacaktır.

## 2. Eğitim yöntemi

Her aşama küçük ve anlaşılır tutulacaktır. Yalnızca kodun ne olduğu değil, neden o şekilde yazıldığı da açıklanacaktır.

Her geliştirme döngüsü şu sırayı izler:

1. Yapılacak küçük özellik tanımlanır.
2. Gerekli Android veya yazılım geliştirme kavramı açıklanır.
3. Kod bir `feature/...` dalında geliştirilir.
4. Otomatik testler çalıştırılır.
5. GitHub Actions ile APK üretilir.
6. Mümkün olan ekran akışları sanal cihazda doğrulanır.
7. Gerçek donanım gerektiren aşamalar fiziksel telefonda denenir.
8. Öğrenilenler ve alınan kararlar dokümana yazılır.
9. Pull Request açılarak değişiklikler `main` ile karşılaştırılır.
10. Sonuç kabul edildikten sonra `main` dalına alınır.

## 3. Branch yapısı

### `main`

Projenin kabul edilmiş ve çalışan durumunu temsil eder. Deneysel veya tamamlanmamış çalışmalar doğrudan `main` üzerinde yapılmaz.

### `feature/android-v1`

İlk Android uygulama iskeletinin geliştirildiği daldır. Bu dal `main` üzerinden oluşturulmuştur.

## 4. İlk teknik teslim: Android V1 iskeleti

İlk teslimde gerçek PaperLit özelliklerinden önce geliştirme ve dağıtım hattının çalıştığı kanıtlandı:

- Kotlin ve Jetpack Compose projesi
- Uygulamanın başarıyla açılması
- Basit bir geçici ekran
- Uygulama sürüm bilgisinin gösterilmesi
- Yerel birim testleri
- Sanal cihazda Compose arayüz testi
- GitHub Actions ile başarılı Android derlemesi
- İndirilebilir debug APK

İlk iskelette kullanılan `LibraryUiState` ve “Kütüphanem boş” metni gerçek PaperLit tasarımı değildir. Yanlış ürün varsayımından kalan geçici test ekranıdır ve gerçek tarama akışına geçerken kaldırılacaktır.

## 5. Doğru ürün akışı

```text
Ambalaj fotoğrafı
    ↓
OCR ile aday alanlar
    ↓
Kullanıcı doğrulaması/düzeltmesi
    ↓
Fiyat girişi veya ayrı fiyat etiketi okuması
    ↓
Birim maliyet ve fiyat/performans hesabı
    ↓
SQLite'a yeni gözlem kaydı
    ↓
Geçmiş ürünlerle karşılaştırma/güncelleme
```

OCR çıktısı kullanıcı onayı olmadan kesin veri kabul edilmez.

## 6. İlk aşamada öğrenilecek kavramlar

- Android projesinin temel klasör yapısı
- Kotlin, Jetpack Compose ve Gradle'ın görevleri
- APK'nın ne olduğu ve nasıl üretildiği
- `main` ile `feature` dalları arasındaki fark
- Commit ve Pull Request kavramları
- GitHub Actions iş akışı
- Birim testi ile arayüz testi arasındaki fark
- Kamera izinleri ve görüntü alma
- OCR sonucu ile doğrulanmış veri arasındaki fark
- SQLite üzerinde yerel veri saklama

## 7. Öngörülen geliştirme sırası

1. Android V1 teknik iskeleti — tamamlandı
2. GitHub üzerinde sanal cihaz testi — tamamlandı
3. Gerçek ürün gereksinimleri ve kullanıcı akışı
4. İlk ürün türü ve veri alanlarının kesinleştirilmesi
5. Birim maliyet/fiyat-performans hesaplarının örneklerle doğrulanması
6. Gerçek PaperLit başlangıç ve veri onay ekranı
7. Kamera ile ambalaj görüntüsü alma
8. OCR ile ambalaj bilgisi çıkarma
9. Eksik alanların elle girişi ve geçmiş veriden seçim
10. Fiyatın elle girilmesi
11. SQLite veri modeli ve geçmiş gözlemler
12. Eski ürünü bulma, karşılaştırma ve güncelleme
13. Ayrı raf/fiyat etiketi kamera okuması
14. Fiziksel telefon ve market saha testi

Bu sıra öğrenme ve test sonuçlarına göre ayrıntılandırılabilir. Uygulamanın yerel çalışma ve hosting gerektirmeme kuralları korunur.

## 8. Öğrencinin aktif rolü

Geliştirme sırasında kullanıcı yalnızca hazır APK'yı deneyen kişi olmayacaktır:

- GitHub değişikliklerini inceleyecek,
- Yapılan işi kendi cümleleriyle açıklamaya çalışacak,
- Hesaplama örneklerinin doğruluğunu kontrol edecek,
- Sanal cihaz ve zamanı geldiğinde fiziksel telefon sonuçlarını karşılaştıracak,
- Küçük kod, metin veya ayar değişiklikleri yapacak,
- Pull Request içindeki dosya farklarını okuyacaktır.

## 9. Bir bölümün tamamlanma ölçütü

Bir geliştirme bölümü ancak ilgili koşullar sağlandığında tamamlanmış sayılır:

- Kod hedeflenen davranışı sağlıyor.
- İlgili otomatik testler geçiyor.
- GitHub Actions derlemesi başarılı.
- APK üretilebiliyor.
- Sanal cihaz testi yapıldı.
- Fiziksel cihaz gerektiren özelliklerde saha testi yapıldı veya neden ertelendiği kaydedildi.
- Tutorial bölümü güncellendi.
- Değişiklikler Pull Request üzerinden incelendi.

## 10. Sıradaki adım

İlk karşılaştırılacak ürün türü, zorunlu/isteğe bağlı ambalaj alanları ve hesaplama kuralları örnek ürünler üzerinden kesinleştirilecektir. Bunlar netleşmeden SQLite şeması veya OCR ekranı tasarlanmayacaktır.
