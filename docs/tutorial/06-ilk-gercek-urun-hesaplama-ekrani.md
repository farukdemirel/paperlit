# 06 — İlk Gerçek Ürün Bilgisi ve Hesaplama Ekranı

## 1. Bu bölümde ne yaptık?

Yanlış ürün varsayımından kalan `LibraryUiState` ve “Kütüphanem boş” ekranı kaldırıldı. Yerine tuvalet kâğıdı pilot ürünü için düzenlenebilir bilgi ve hesaplama ekranı eklendi.

Bu ekran henüz kamera veya OCR kullanmaz. Ama ileride OCR'ın dolduracağı alanları şimdiden görünür ve kullanıcı tarafından düzeltilebilir hale getirir.

## 2. Ekrandaki alanlar

- Marka
- Paket fiyatı
- Rulo sayısı
- Rulo başına yaprak
- Kat sayısı
- Yaprak eni
- Yaprak boyu

İlk açılışta 05. bölümde doğruladığımız örnek paket değerleri hazır gelir:

- Örnek Marka
- 240 TL
- 16 rulo
- 150 yaprak/rulo
- 3 kat
- 95 × 120 mm

Kullanıcı bu değerlerin tamamını değiştirebilir.

## 3. Hesapla düğmesinin akışı

```text
Metin alanları
    ↓
Sayı ve fiyat doğrulaması
    ↓
ToiletPaperPackage
    ↓
ToiletPaperValueCalculator
    ↓
Sonuç kartı
```

Arayüz hesaplama formüllerini yeniden yazmaz. 05. bölümde birim testleriyle doğrulanan domain hesaplayıcısını çağırır. Böylece ekran ve hesap motoru aynı kuralı kullanır.

## 4. Para girişi

Kullanıcı fiyatı TL olarak girer; hem virgül hem nokta ondalık ayırıcı kabul edilir. Değer hesaplayıcıya gönderilmeden önce kuruşa çevrilir.

Örnek:

```text
240,50 TL → 24.050 kuruş
```

Yuvarlama en yakın kuruşa yapılır.

## 5. Eksik ve hatalı alanlar

Marka boş bırakılabilir. Yaprak, kat ve ölçü alanları da ambalajda yoksa boş kalabilir.

Rulo sayısı ve paket fiyatı hesap için zorunludur. Sıfır, negatif veya sayı olmayan değerler kabul edilmez. Hata oluşursa kullanıcıya Türkçe açıklama gösterilir ve eski sonuç temizlenir.

## 6. Sonuç kartı

Bilgiler yeterliyse sonuç kartında şunlar gösterilir:

- Marka
- Toplam yaprak
- Rulo başına fiyat
- 100 yaprak başına fiyat
- 1 m² başına fiyat
- Kat eşdeğerli 1 m² başına fiyat

Ekran ayrıca kat eşdeğerinin kalite puanı olmadığını açıkça belirtir.

## 7. Compose durum yönetimi

Metin girişleri `rememberSaveable` ile tutulur. Böylece Android ekranı yeniden oluşturduğunda kullanıcının yazdığı basit metinler korunabilir.

Hesap sonucu olan özel Kotlin nesnesi `remember` ile tutulur. Bu nesne Android'in otomatik kaydetme sistemine doğrudan verilmez. Kalıcı veri saklama ileride SQLite katmanının görevi olacaktır.

## 8. Sanal cihaz testi

Compose testi:

1. PaperLit başlığını kontrol eder.
2. Tuvalet kâğıdı karşılaştırma ekranını doğrular.
3. `Hesapla` düğmesine basar.
4. Örnek paket için şu sonuçları arar:
   - 2.400 toplam yaprak
   - 15,00 TL/rulo
   - 10,00 TL/100 yaprak
   - 2,92 TL/kat eşdeğerli m²

Bu test GitHub Actions üzerindeki Pixel 2 / Android API 35 sanal cihazında çalışır.

## 9. Tamamlanma ölçütü

- Yanlış `LibraryUiState` kaynak ve test dosyaları kaldırılmıştır.
- Gerçek ürün formu derlenmektedir.
- Domain birim testleri geçmektedir.
- Compose ekran testi sanal cihazda geçmektedir.
- Debug APK üretilmektedir.
- GitHub Actions yeşildir.

## 10. Mevcut durum

Kod ve testler `feature/android-v1` dalına eklenmiştir. GitHub Actions sonucu henüz doğrulanmadığı için bu bölüm **doğrulama bekliyor** durumundadır.

## 11. Sıradaki adım

Ekran doğrulandıktan sonra ürün formu ile OCR arasında kullanılacak aday alan modeli tasarlanacaktır. Daha sonra örnek ambalaj fotoğraflarından OCR denemelerine geçilecektir.
