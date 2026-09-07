# 05 — Pilot Ürün ve Hesaplama Çekirdeği

## 1. Pilot ürün kararı

PaperLit'in ilk çalışan ürün modeli **tuvalet kâğıdı** olacaktır.

Tek ürün grubuyla başlamamızın nedeni; OCR, kullanıcı doğrulaması, hesaplama ve SQLite katmanlarını aynı anda gereksiz büyütmeden gerçek bir örnek üzerinde geliştirmektir. Tuvalet kâğıdı modeli doğrulandıktan sonra ortak alanlar korunarak kâğıt havlu gibi diğer rulo ürünlere genişletilecektir.

## 2. İlk veri alanları

`ToiletPaperPackage` şu doğrulanmış bilgileri taşır:

- Marka — isteğe bağlı
- Ürün adı/varyant — isteğe bağlı
- Paket içindeki rulo sayısı — zorunlu
- Rulo başına yaprak sayısı — ambalajda varsa
- Kat sayısı — ambalajda varsa
- Yaprak eni — ambalajda varsa
- Yaprak boyu — ambalajda varsa
- Rulo başına toplam uzunluk — ambalajda varsa

Alanların çoğu isteğe bağlıdır; çünkü her ambalaj aynı bilgileri yayımlamaz. Uygulama eksik veriyi uydurmaz. Yalnız mevcut alanlarla güvenilir hesaplanabilen metrikleri üretir.

## 3. Neden OCR modeli değil?

Bu sınıf OCR'ın ham tahminini değil, kullanıcının kontrol edip doğruladığı paket bilgisini temsil eder.

Doğru akış:

```text
OCR tahmini → düzenlenebilir onay ekranı → doğrulanmış ToiletPaperPackage
```

Örneğin OCR `3 kat` ifadesini `8 kat` okuyabilir. Bu hata kullanıcı onayı olmadan hesaplama modeline aktarılırsa bütün karşılaştırma bozulur.

## 4. Hesaplanan miktarlar

### Toplam yaprak

```text
Toplam yaprak = Rulo sayısı × Rulo başına yaprak
```

### Toplam uzunluk

Ambalaj rulo uzunluğunu veriyorsa:

```text
Toplam uzunluk = Rulo sayısı × Rulo başına uzunluk
```

Uzunluk verilmemiş; yaprak sayısı ve yaprak boyu verilmişse:

```text
Toplam uzunluk =
    Rulo sayısı × Rulo başına yaprak × Yaprak boyu
```

### Toplam yüzey

Yaprak eni ve toplam uzunluk biliniyorsa:

```text
Toplam yüzey = Yaprak eni × Toplam uzunluk
```

### Kat eşdeğerli yüzey

```text
Kat eşdeğerli yüzey = Toplam yüzey × Kat sayısı
```

Bu değer kalite puanı değildir. Yalnız kat sayısını da içeren karşılaştırılabilir bir kâğıt miktarı göstergesidir. Kalınlık, emicilik, dayanıklılık ve yumuşaklık bu hesapta ölçülmez.

## 5. Fiyat metrikleri

Fiyat para biriminin en küçük birimiyle, yani Türk lirası için **kuruş** olarak tutulur. Böylece para değerlerinde kayan noktalı sayı kaynaklı saklama hataları önlenir.

Bilgiler yeterliyse şu metrikler üretilir:

- Rulo başına fiyat
- 100 yaprak başına fiyat
- Metrekare başına fiyat
- Kat eşdeğerli metrekare başına fiyat

Eksik bilgi gerektiren bir metrik `null` kalır. Örneğin yaprak ölçüsü yoksa uygulama yüzey alanı uydurmaz.

## 6. Birim testi örneği

Örnek paket:

- 16 rulo
- Rulo başına 150 yaprak
- 3 kat
- Yaprak ölçüsü 95 × 120 mm
- Paket fiyatı 240 TL

Beklenen sonuçlar:

- Toplam yaprak: 2.400
- Toplam uzunluk: 288 m
- Toplam yüzey: 27,36 m²
- Kat eşdeğerli yüzey: 82,08 m²-kat
- Rulo başına fiyat: 15 TL
- 100 yaprak başına fiyat: 10 TL

Birim testi bu sonuçları otomatik olarak doğrular.

## 7. Ek test senaryoları

Testler ayrıca şunları kontrol eder:

- Ambalaj yalnız rulo uzunluğunu verdiğinde alan hesabı
- Ölçüler eksikken hesaplanamayan metriklerin boş kalması
- Sıfır rulo sayısının reddedilmesi
- Sıfır fiyatın reddedilmesi

## 8. Dosyalar

- `domain/ToiletPaper.kt`: Doğrulanmış paket modeli ve hesaplayıcı
- `domain/ToiletPaperValueCalculatorTest.kt`: Saf Kotlin birim testleri

Bu kod Android ekranından, kameradan, OCR motorundan ve SQLite'tan bağımsızdır. Böylece temel matematik hızlı ve güvenilir biçimde test edilebilir.

## 9. Tamamlanma ölçütü

Bu bölüm şu koşullarda tamamlanmış sayılır:

- Pilot ürün kararı dokümana yazılmıştır.
- Hesap modeli derlenmektedir.
- Dört birim test senaryosu geçmektedir.
- GitHub Actions yeşildir.
- Örnek hesap kullanıcı tarafından mantıksal olarak kabul edilmiştir.

## 10. Sıradaki adım

Hesaplama çekirdeği doğrulandıktan sonra gerçek PaperLit başlangıç ekranı ve ürün bilgisi onay formunun ilk sürümü tasarlanacaktır. Kamera/OCR henüz eklenmeden, form örnek veriyle çalıştırılacak ve sanal cihazda test edilecektir.
