# 04 — Telefonsuz Test: GitHub Üzerinde Sanal Android Cihazı

## 1. Bu bölümde ne yapıyoruz?

PaperLit'in Compose arayüz testini fiziksel telefon olmadan çalıştırıyoruz. GitHub Actions, geçici bir Android sanal cihazı oluşturacak; uygulamayı bu cihaza kuracak ve `PaperLitAppTest` testini çalıştıracaktır.

Bu aşama gerçek telefon testinin tamamen yerine geçmez. Amacı, temel ekranın otomatik ve tekrarlanabilir şekilde açıldığını kanıtlamaktır.

## 2. Neleri doğruluyoruz?

Mevcut arayüz testi sanal cihaz ekranında şu metinleri arar:

- `PaperLit`
- `Kütüphanem boş`
- `Sürüm 0.1.0`

Bu metinlerden biri bulunamazsa test başarısız olur ve APK kabul edilmez.

## 3. Gradle Managed Device nedir?

Gradle Managed Device, test için gereken Android sanal cihazının Gradle tarafından tanımlanıp yönetilmesini sağlar.

PaperLit için ilk test cihazı:

- Cihaz profili: Pixel 2
- Android API seviyesi: 35
- Sistem görüntüsü: AOSP
- Gradle cihaz adı: `pixel2Api35`

Cihaz kalıcı değildir. GitHub Actions çalışması sırasında hazırlanır ve çalışma bittiğinde kaldırılır.

## 4. Projede yapılan değişiklikler

`app/build.gradle.kts` dosyasına `pixel2Api35` adlı yönetilen sanal cihaz eklendi.

`.github/workflows/android.yml` dosyasına şu test komutu eklendi:

```bash
./gradlew pixel2Api35DebugAndroidTest
```

CI sırası artık şöyledir:

1. Birim testlerini çalıştır.
2. Sanal Android cihazında Compose arayüz testini çalıştır.
3. Debug APK üret.
4. APK ve test raporlarını artifact olarak yükle.

## 5. Bu test neyi kanıtlar, neyi kanıtlamaz?

Kanıtladıkları:

- Uygulama sanal Android ortamına kurulabiliyor.
- Başlangıç ekranı açılabiliyor.
- Beklenen temel metinler ekranda oluşuyor.
- Compose arayüz testi geçiyor.

Kanıtlamadıkları:

- Gerçek telefondaki kamera, barkod okuyucu veya dosya seçici davranışı
- Farklı üreticilerin cihazlarına özgü sorunlar
- Market içindeki gerçek kullanım rahatlığı
- Pil, performans ve saha koşulları

Bu nedenle ileride gerçek özellikler eklendiğinde fiziksel telefon ve saha testi ayrıca yapılacaktır.

## 6. Tamamlanma ölçütü

Bu bölüm ancak aşağıdakiler doğrulandığında tamamlanmış sayılır:

- GitHub Actions çalışması başlamıştır.
- Yerel birim testleri geçmiştir.
- `pixel2Api35DebugAndroidTest` başarılıdır.
- Debug APK üretilmiştir.
- APK artifact olarak indirilebilir durumdadır.
- Test raporları artifact olarak kaydedilmiştir.

## 7. Mevcut durum

Sanal cihaz ve CI yapılandırması repoya eklenmiştir. GitHub Actions sonucu henüz doğrulanmadığı için bölüm şu anda **doğrulama bekliyor** durumundadır.

Başarılı çalışma görüldüğünde bu bölümün sonucu güncellenecek ve ilk Android iskeletinin telefonsuz doğrulaması tamamlanacaktır.
