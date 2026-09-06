# 04 — Telefonsuz Test: GitHub Üzerinde Sanal Android Cihazı

> **Kapsam notu:** Bu bölümde doğrulanan “Kütüphanem boş” ekranı gerçek ürün tasarımı değil, geçici teknik iskelettir. Test altyapısı geçerlidir ve gerçek PaperLit kamera/OCR/fiyat karşılaştırma ekranlarına uygulanacaktır. Bağlayıcı kapsam: [Kanonik Ürün Tanımı](../PRODUCT_DEFINITION.md).

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

`app/build.gradle.kts` dosyasına `pixel2Api35` adlı yerel yönetilen sanal cihaz eklendi. AGP 9.3 Kotlin DSL yapısı şöyledir:

```kotlin
testOptions {
    managedDevices {
        localDevices {
            create("pixel2Api35") {
                device = "Pixel 2"
                apiLevel = 35
                systemImageSource = "aosp"
            }
        }
    }
}
```

`.github/workflows/android.yml` dosyasında test şu komutla çalıştırılır:

```bash
./gradlew pixel2Api35DebugAndroidTest -Pandroid.testoptions.manageddevices.emulator.gpu=swiftshader_indirect
```

`swiftshader_indirect`, GitHub Actions sunucusunda donanım görüntüleme desteğine güvenmeden emülatörün yazılım GPU ile çalışmasını sağlar.

CI sırası artık şöyledir:

1. Birim testlerini çalıştır.
2. Sanal Android cihazında Compose arayüz testini çalıştır.
3. Debug APK üret.
4. APK ve test raporlarını artifact olarak yükle.

## 5. İlk deneme ve öğrendiğimiz hata

İlk denemede eski DSL biçimi olan `managedDevices { devices { ... } }` kullanıldı. AGP 9.3, `devices` alanını tanımadığı için Gradle daha testlere başlamadan yapılandırma aşamasında durdu:

```text
Unresolved reference 'devices'
```

Güncel Android dokümantasyonuna göre yapı `managedDevices { localDevices { ... } }` olarak düzeltildi. Ayrıca GitHub Actions için önerilen yazılım GPU parametresi eklendi.

Bu örnek, CI hatasının her zaman uygulama kodundaki bir hata olmadığını gösterir. Bu kez hata test cihazının Gradle yapılandırmasındaydı.

## 6. Bu test neyi kanıtlar, neyi kanıtlamaz?

Kanıtladıkları:

- Uygulama sanal Android ortamına kurulabiliyor.
- Başlangıç ekranı açılabiliyor.
- Beklenen temel metinler ekranda oluşuyor.
- Compose arayüz testi geçiyor.

Kanıtlamadıkları:

- Gerçek telefondaki kamera ve OCR davranışı
- Farklı üreticilerin cihazlarına özgü sorunlar
- Market içindeki gerçek kullanım rahatlığı
- Pil, performans ve saha koşulları

Bu nedenle ileride gerçek özellikler eklendiğinde fiziksel telefon ve saha testi ayrıca yapılacaktır.

## 7. Tamamlanma ölçütü

Bu bölüm ancak aşağıdakiler doğrulandığında tamamlanmış sayılır:

- GitHub Actions çalışması başlamıştır.
- Yerel birim testleri geçmiştir.
- `pixel2Api35DebugAndroidTest` başarılıdır.
- Debug APK üretilmiştir.
- APK artifact olarak indirilebilir durumdadır.
- Test raporları artifact olarak kaydedilmiştir.

## 8. Doğrulama sonucu

GitHub Actions çalışmaları başarıyla tamamlandı:

- AGP 9.3 sanal cihaz DSL yapılandırması kabul edildi.
- Yerel birim testleri geçti.
- Pixel 2 / Android API 35 sanal cihazı hazırlandı.
- Compose arayüz testi sanal cihazda geçti.
- GitHub Actions için yazılım GPU ayarıyla çalışma başarılı oldu.
- Debug APK üretildi.
- APK ve test raporları artifact olarak kaydedildi.

Böylece ilk Android iskeletinin **telefonsuz otomatik doğrulaması tamamlandı**. Gerçek telefon ve saha testi, cihaz donanımına veya gerçek kullanım koşullarına bağlı özellikler geliştirildiğinde ayrıca yapılacaktır.
