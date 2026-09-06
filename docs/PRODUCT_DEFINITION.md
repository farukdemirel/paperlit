# PaperLit — Kanonik Ürün Tanımı

## 1. Belgenin görevi

Bu belge PaperLit'in bağlayıcı ürün tanımıdır. Kod, tutorial, issue veya başka bir açıklama bu belgeyle çelişirse önce çelişki giderilmeden geliştirmeye devam edilmez.

**PaperLit bir PDF, e-kitap, belge yönetimi veya belge okuma uygulaması değildir.**

## 2. Ürün amacı

PaperLit, markette karşılaşılan ambalajlı ürünlerin gerçek miktarını ve fiyatını karşılaştırmayı kolaylaştıran yerel bir Android uygulamasıdır.

İlk odak, ambalajında rulo sayısı, yaprak sayısı, kat sayısı, yaprak ölçüsü veya toplam uzunluk gibi karşılaştırılabilir bilgiler bulunan ürünlerdir.

Uygulama:

- Ürün ambalajını telefon kamerasıyla görüntüler.
- Ambalaj metnini OCR ile okumaya çalışır.
- Hesaplama için gerekli alanları yapılandırılmış verilere dönüştürür.
- OCR ile bulunamayan veya güvenilir okunamayan alanları kullanıcıya tamamlattırır.
- Girilen fiyatla nesnel birim maliyetleri hesaplar.
- Her doğrulanmış okumayı yerel SQLite veritabanında saklar.
- Daha önce kaydedilmiş ürünleri yeniden bulur, karşılaştırır ve gerektiğinde günceller.
- İnternet ve hosting zorunluluğu olmadan çalışır.

## 3. Temel kullanıcı akışı

1. Kullanıcı yeni ürün okuması başlatır.
2. Ambalaj fotoğrafı çekilir.
3. OCR, ambalajdaki aday bilgileri çıkarır.
4. Uygulama bulunan alanları düzenlenebilir bir onay ekranında gösterir.
5. Kullanıcı eksik veya yanlış bilgileri düzeltir.
6. Fiyat elle girilir veya ayrı bir kamera okumasıyla fiyat etiketi okunur.
7. Birim maliyet ve karşılaştırma değerleri hesaplanır.
8. Kullanıcı sonucu onaylar.
9. Okuma SQLite veritabanına kaydedilir.
10. Ürün daha önce kayıtlıysa eski bilgi kullanılabilir, yeni gözlem eklenebilir veya ürünün güncel bilgileri yenilenebilir.

OCR çıktısı kullanıcı onayı olmadan kesin veri kabul edilmez.

## 4. Okunması hedeflenen bilgiler

Ambalajın sunduğu bilgiye göre aşağıdaki alanlardan mümkün olanlar okunacaktır:

- Marka
- Ürün adı
- Ürün çeşidi veya varyantı
- Paket içindeki rulo/adet sayısı
- Rulo başına yaprak sayısı
- Kat sayısı
- Yaprak eni ve boyu
- Rulo başına veya paket toplam uzunluğu
- Ambalajda bulunan diğer karşılaştırılabilir miktar bilgileri

Her ürün bütün alanları vermeyebilir. Veri modeli eksik alanları desteklemelidir.

## 5. Marka ve fiyat girişi

### Marka

Marka OCR ile güvenilir biçimde elde edilemezse:

- Kullanıcı markayı elle yazabilir.
- Daha önce biriken marka listesinden seçim yapabilir.
- Yeni yazılan marka sonraki okumalar için saklanabilir.

### Fiyat

Fiyat ambalajdan farklı bir yerde bulunabileceği için:

- Temel ve güvenilir yöntem elle fiyat girişidir.
- Ayrı bir kamera okumasıyla raf veya fiyat etiketini okuma daha sonra eklenebilir.
- Kamera ile bulunan fiyat da kaydetmeden önce kullanıcı tarafından doğrulanır.

## 6. Hesaplama ilkeleri

Uygulama mümkün olduğunca açıklanabilir değerler üretir. Kullanıcı yalnız tek bir kapalı “puan” görmemeli; karşılaştırmanın nedenini anlayabilmelidir.

Bilgiler mevcutsa hesaplanabilecek örnekler:

- Rulo/adet başına fiyat
- 100 yaprak başına fiyat
- Paket toplam yaprak sayısı
- Toplam kâğıt yüzeyi
- Kat sayısı hesaba katılmış eşdeğer yüzey
- Birim uzunluk veya birim yüzey maliyeti
- Benzer ürünler arasındaki fiyat avantajı

Temel örnekler:

```text
Toplam yaprak = Rulo sayısı × Rulo başına yaprak
Toplam yüzey = Toplam yaprak × Yaprak eni × Yaprak boyu
Kat eşdeğerli yüzey = Toplam yüzey × Kat sayısı
```

Kat sayısı tek başına kaliteyi tam ölçmez. Kâğıt kalınlığı, emicilik ve dayanıklılık gibi ambalajdan güvenilir çıkarılamayan özellikler ayrıca tanımlanmadan kesin kalite varsayımı yapılmaz.

Nihai fiyat/performans puanının formülü ayrı bir ürün kararı olarak belirlenecek ve kullanıcıya açıklanabilir olacaktır.

## 7. Yerel veri saklama

Veriler cihazdaki SQLite veritabanında tutulacaktır. Android Room kullanımı teknik tasarım aşamasında değerlendirilebilir; bağlayıcı gereksinim yerel SQLite saklamadır.

En az şu kavramların birbirinden ayrılması hedeflenir:

- Ürün ve marka ana bilgileri
- Ambalaj/miktar bilgileri
- Her yeni okuma veya gözlem
- Fiyat ve gözlem tarihi
- OCR tarafından önerilen değerler
- Kullanıcının doğruladığı nihai değerler

Yeni bir okuma önceki fiyat geçmişini sessizce yok etmemelidir. Aynı ürün yeniden görüldüğünde geçmiş ile güncel gözlem karşılaştırılabilmelidir.

## 8. Çevrimdışı çalışma

Temel kullanım için:

- Kullanıcı hesabı gerekmeyecek.
- Sunucu veya hosting gerekmeyecek.
- İnternet bağlantısı zorunlu olmayacak.
- Ürün ve fiyat geçmişi telefonda saklanacak.

İleride isteğe bağlı yedekleme veya senkronizasyon düşünülse bile temel yerel çalışma özelliği korunacaktır.

## 9. Test yaklaşımı

- OCR dışındaki hesaplama kuralları otomatik birim testleriyle doğrulanır.
- Ekran akışları sanal Android cihazında test edilir.
- Kamera ve OCR için örnek ambalaj görüntüleriyle tekrarlanabilir testler hazırlanır.
- Gerçek kamera, raf etiketi ve market kullanım kolaylığı fiziksel telefonla saha testinde doğrulanır.

## 10. Mevcut teknik iskelenin durumu

`feature/android-v1` dalındaki Kotlin, Jetpack Compose, Gradle, GitHub Actions, sanal cihaz ve APK üretim altyapısı geçerlidir ve korunacaktır.

Mevcut `LibraryUiState`, “Kütüphanem boş” metni ve belge odaklı adlandırmalar ürün gereksinimi değildir. Bunlar ilk Android derleme ve test hattını kanıtlayan geçici iskelettir ve gerçek PaperLit ekranına geçilirken değiştirilecektir.

## 11. Sıradaki ürün adımı

Kodlamadan önce:

1. Karşılaştırılacak ilk ürün türü kesinleştirilecek.
2. Zorunlu ve isteğe bağlı veri alanları belirlenecek.
3. Birim maliyet formülleri örnek ürünlerle doğrulanacak.
4. OCR onay ekranının kullanıcı akışı tasarlanacak.
5. Ardından gerçek PaperLit veri modeli ve başlangıç ekranı geliştirilecektir.
