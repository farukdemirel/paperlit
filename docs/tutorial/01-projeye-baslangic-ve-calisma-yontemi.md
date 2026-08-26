# 01 — Projeye Başlangıç ve Çalışma Yöntemi

## 1. Projenin amacı

PaperLit, Android üzerinde çalışacak ve kullanıcının belgelerini cihazında yönetip okuyabilmesini sağlayacak bir uygulama olarak geliştirilecektir.

Bu proje aceleyle tamamlanacak tek seferlik bir kod üretimi değildir. Aynı zamanda uygulamalı bir Android geliştirme eğitimidir.

Başlangıçta bilgisayarda Android geliştirme ve derleme ortamının bulunması zorunlu değildir. İlk APK'lar GitHub Actions kullanılarak üretilebilir. Yerel Android geliştirme ortamı daha sonraki bir aşamada ayrıca kurulup öğrenilebilir.

## 2. Eğitim yöntemi

Her aşama küçük ve anlaşılır tutulacaktır. Yalnızca kodun ne olduğu değil, neden o şekilde yazıldığı da açıklanacaktır.

Her geliştirme döngüsü şu sırayı izler:

1. Yapılacak küçük özellik tanımlanır.
2. Gerekli Android veya yazılım geliştirme kavramı açıklanır.
3. Kod bir `feature/...` dalında geliştirilir.
4. Otomatik testler çalıştırılır.
5. GitHub Actions ile APK üretilir.
6. APK gerçek telefonda denenir.
7. Öğrenilenler ve alınan kararlar dokümana yazılır.
8. Pull Request açılarak değişiklikler `main` ile karşılaştırılır.
9. Sonuç kabul edildikten sonra `main` dalına alınır.

## 3. Branch yapısı

### `main`

Projenin kabul edilmiş ve çalışan durumunu temsil eder. Deneysel veya tamamlanmamış çalışmalar doğrudan `main` üzerinde yapılmaz.

### `feature/android-v1`

İlk Android uygulama iskeletinin geliştirileceği daldır. Bu dal `main` üzerinden oluşturulmuştur.

Başlangıç anında:

- `Behind: 0`
- `Ahead: 0`

değerleri dalın `main` ile aynı commit'ten başladığını gösterir.

## 4. İlk teslim: Android V1 iskeleti

İlk sürüm, gerçek PaperLit özelliklerinin tamamını içermeyecektir. Önce bütün geliştirme ve dağıtım hattının çalıştığı kanıtlanacaktır.

İlk APK'da hedeflenenler:

- Uygulamanın başarıyla açılması
- PaperLit başlığının görüntülenmesi
- Basit bir ana ekran
- “Kütüphanem boş” başlangıç durumu
- Uygulama sürüm bilgisinin gösterilmesi
- Temel birim testleri
- Temel arayüz testi
- GitHub Actions ile başarılı Android derlemesi
- İndirilebilir ve telefona kurulabilir APK

Akış şöyledir:

```text
Kaynak kod
    ↓
GitHub
    ↓
Otomatik test
    ↓
Android derleme
    ↓
APK
    ↓
Telefonda deneme
```

Bu zincir başarıyla çalışmadan belge ekleme, kütüphane veya okuma ekranı gibi daha büyük özelliklere geçilmez.

## 5. İlk aşamada öğrenilecek kavramlar

- Android projesinin temel klasör yapısı
- Gradle'ın görevi
- APK'nın ne olduğu ve nasıl üretildiği
- `main` ile `feature` dalları arasındaki fark
- Commit kavramı
- Pull Request'in amacı
- GitHub Actions iş akışı
- Birim testi ile arayüz testi arasındaki fark
- APK'nın Android telefona kurulması

Bu kavramlar toplu ve soyut bir ders şeklinde anlatılmayacaktır. Her biri projede kullanıldığı anda, çalışan örnek üzerinden ele alınacaktır.

## 6. Öngörülen geliştirme sırası

1. Android V1 iskeleti
2. Yerel kütüphane veri modeli
3. Cihazdan belge ekleme
4. Kütüphane ekranı
5. Okuma ekranı
6. Okuma konumunu cihazda saklama
7. Arama, sıralama ve kitap bilgileri
8. Yerel yedekleme ve dışa aktarma

Bu sıra ihtiyaçlara ve öğrenme sürecine göre ayrıntılandırılabilir. Ancak uygulamanın yerel çalışma ve hosting gerektirmeme kuralları korunur.

## 7. Öğrencinin aktif rolü

Geliştirme sırasında kullanıcı yalnızca hazır APK'yı deneyen kişi olmayacaktır. Her aşamada:

- Değişiklikleri GitHub üzerinden inceleyecek,
- Yapılan işi kendi cümleleriyle açıklamaya çalışacak,
- APK'yı telefonda test edecek,
- Beklenen ve gerçekleşen davranışı karşılaştıracak,
- Zaman zaman küçük kod, metin veya ayar değişiklikleri yapacak,
- Pull Request içindeki dosya farklarını okuyacaktır.

## 8. Bir bölümün tamamlanma ölçütü

Bir geliştirme bölümü ancak aşağıdaki koşullar sağlandığında tamamlanmış sayılır:

- Kod hedeflenen davranışı sağlıyor.
- İlgili otomatik testler geçiyor.
- GitHub Actions derlemesi başarılı.
- APK üretilebiliyor.
- Gerçek cihaz testi yapıldı veya neden yapılamadığı kaydedildi.
- Tutorial bölümü güncellendi.
- Değişiklikler Pull Request üzerinden incelendi.

## 9. Sıradaki adım

`feature/android-v1` dalında temel Android proje yapısı oluşturulacak. İlk ekran, test altyapısı ve APK üreten GitHub Actions iş akışı küçük ve açıklanabilir commit'lere ayrılacaktır.
