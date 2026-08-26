# 02 — Android Teknoloji Seçimi ve Proje Yapısı

## 1. Bu bölümde ne yapıyoruz?

Bu bölümde henüz uygulama ekranını yazmıyoruz. Önce PaperLit'in hangi Android teknolojileriyle geliştirileceğini ve proje klasörlerinin ne anlama geleceğini kararlaştırıyoruz.

Bu karar önemlidir; çünkü oluşturacağımız ilk proje dosyaları seçilen dil, arayüz sistemi ve derleme araçlarına göre şekillenecektir.

## 2. Teknoloji kararlarımız

| Konu | Karar | Neden? |
|---|---|---|
| Programlama dili | Kotlin | Android'in güncel geliştirme dili ve Compose'un doğal dili |
| Kullanıcı arayüzü | Jetpack Compose | Android'in önerilen modern yerel UI sistemi |
| Tasarım bileşenleri | Material 3 | Compose ile uyumlu güncel Android bileşenleri |
| Gradle dosyaları | Kotlin DSL (`.kts`) | Yapılandırmayı da Kotlin benzeri, tür kontrollü sözdizimiyle yazmak |
| Proje yapısı | Başlangıçta tek `app` modülü | İlk sürümde gereksiz karmaşıklığı önlemek |
| Mimari yön | UI katmanı + Data katmanı | Ekran ile veriyi birbirinden ayırmak |
| Domain katmanı | Şimdilik yok | Gerçek bir ihtiyaç oluşmadan fazladan katman üretmemek |
| Çalışma biçimi | Tamamen yerel | Hosting veya zorunlu sunucu bağımlılığı oluşturmamak |
| Derleme | Gradle Wrapper + GitHub Actions | Bilgisayarda Android ortamı olmasa da tekrarlanabilir APK üretmek |
| Java çalışma ortamı | JDK 17 | Seçilen güncel Android Gradle Plugin sürümünün gereksinimi |

Google, Jetpack Compose'u Android için önerilen modern yerel arayüz aracı olarak tanımlar. Güncel mimari rehberi ise uygulamaların en az bir UI ve bir Data katmanına ayrılmasını önerir.

## 3. Neden Kotlin?

Kotlin, Android geliştirme ekosisteminin temel dilidir. Jetpack Compose API'leri Kotlin düşünce biçimine göre tasarlanmıştır.

Kotlin seçmemizin pratik sonuçları:

- Ekranlarımızı Kotlin ile yazacağız.
- Gradle yapılandırmalarında Kotlin DSL kullanacağız.
- Testlerimizi Kotlin ile yazacağız.
- Java öğrenmek ilk aşama için zorunlu olmayacak.
- Gerektiğinde mevcut Java kütüphanelerini yine kullanabileceğiz.

### İlk Kotlin örneği

```kotlin
val applicationName = "PaperLit"
```

Burada:

- `val`, sonradan başka bir değere atanmayacak değişkeni tanımlar.
- `applicationName`, değişkenin adıdır.
- `"PaperLit"`, değişkenin tuttuğu metindir.

Kotlin ayrıntılarını ezberleyerek başlamayacağız. Projede kullandıkça öğreneceğiz.

## 4. Neden Jetpack Compose?

Eski Android arayüzlerinde ekranlar çoğunlukla XML dosyaları ve bunları yöneten Kotlin/Java sınıflarıyla birlikte hazırlanırdı. Compose'ta arayüzü Kotlin fonksiyonlarıyla tarif ederiz.

Basitleştirilmiş bir örnek:

```kotlin
@Composable
fun PaperLitTitle() {
    Text(text = "PaperLit")
}
```

Bu fonksiyon, ekranda `PaperLit` yazısını göstermeyi tarif eder.

Compose seçmemizin nedenleri:

- Yeni Android projeleri için güncel yaklaşım olması
- Daha az dosyayla ekran oluşturabilmek
- Ekranın durumunu kod üzerinden açıkça takip edebilmek
- Öğrenirken yapılan değişikliklerin sonucunu kolay görebilmek
- Telefon ve daha geniş ekranlara uyarlanabilir arayüz oluşturabilmek

## 5. Gradle nedir?

Gradle, projenin derleme yöneticisidir. Şunları yapar:

- Kullanılan Android ve Kotlin araçlarını belirler.
- Harici kütüphaneleri indirir.
- Kaynak kodu derler.
- Testleri çalıştırır.
- APK üretir.

Gradle ile doğrudan her ayrıntıyı elle yönetmeyiz. Projeye eklenen `gradlew` ve `gradlew.bat` dosyaları, belirlenmiş Gradle sürümünü çalıştırır. Buna **Gradle Wrapper** denir.

Bu sayede geliştirici bilgisayarı ile GitHub Actions aynı Gradle sürümünü kullanabilir.

## 6. Android Gradle Plugin nedir?

Android Gradle Plugin, kısaca **AGP**, normal Gradle'a Android uygulaması üretme yeteneklerini ekler.

İlk iskelette güncel ve sabit bir AGP sürümü sabitlenecektir. Dinamik olarak “en son sürümü bul” anlamına gelen sürüm ifadeleri kullanılmayacaktır; çünkü aynı kaynak kodun farklı günlerde beklenmedik araç sürümleriyle derlenmesini istemiyoruz.

İskelet hazırlanırken hedeflenen araç zinciri:

- Android Gradle Plugin: 9.3.x sabit sürüm
- Gradle: 9.5.0
- JDK: 17
- Gradle yapılandırma dili: Kotlin DSL

Kesin yama sürümleri proje dosyasında açıkça yazılacak ve GitHub Actions derlemesiyle doğrulanacaktır.

## 7. Başlangıç proje yapısı

İlk Android iskeletinin ana görünümü şöyle olacaktır:

```text
paperlit/
├── .github/
│   └── workflows/
│       └── android.yml
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/.../
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle.kts
├── docs/
│   └── tutorial/
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

Şimdi bu parçaların görevlerini inceleyelim.

### `.github/workflows/android.yml`

GitHub Actions iş akışıdır. GitHub'a kod gönderildiğinde testleri ve Android derlemesini otomatik çalıştıracaktır.

### `app/`

Android uygulamasının ana modülüdür. İlk aşamada yalnızca bu modülümüz olacak.

### `app/src/main/`

Telefonda çalışacak gerçek uygulama kodlarını ve kaynaklarını içerir.

### `app/src/test/`

Android cihazı gerektirmeden çalışabilen yerel birim testlerini içerir.

### `app/src/androidTest/`

Android cihazı veya emülatörü üzerinde çalışması gereken testleri içerir.

### `AndroidManifest.xml`

Uygulamanın Android sistemine kendisini tanıttığı dosyadır. Başlangıç ekranı gibi temel bilgiler burada bildirilir.

### `build.gradle.kts`

Derleme ayarlarını ve bağımlılıkları tanımlar. Kök dizindeki dosya proje genelini, `app/` altındaki dosya uygulama modülünü yönetir.

### `settings.gradle.kts`

Projeye hangi modüllerin dahil olduğunu belirtir. İlk aşamada yalnızca `:app` modülü bulunacaktır.

### `gradle/wrapper/`, `gradlew` ve `gradlew.bat`

Projede sabitlenen Gradle sürümünü Linux, macOS ve Windows üzerinde çalıştıran Gradle Wrapper dosyalarıdır.

## 8. Neden şimdilik tek modül?

Büyük Android projelerinde kod birkaç Gradle modülüne bölünebilir. Fakat modül sayısını erkenden artırmak:

- Öğrenilecek kavram sayısını gereksiz büyütür.
- Derleme yapılandırmasını karmaşıklaştırır.
- Henüz var olmayan sınırlar için dosya üretir.

PaperLit V1 küçük başlayacaktır. Kod büyüdüğünde ve gerçek bir ayrım ihtiyacı oluştuğunda modülerleşmeyi ayrı bir tutorial konusu olarak ele alacağız.

## 9. Mimari yönümüz

İlk ekran çok küçük olsa da zihinsel ayrımımız şimdiden bellidir:

```text
UI katmanı
    ↓ kullanıcı olayı
Data katmanı
    ↓ yeni durum
UI katmanı
```

- **UI katmanı:** Ekranda ne gösterileceğini ve kullanıcı olaylarını yönetir.
- **Data katmanı:** Kitaplar, belgeler, okuma konumu ve diğer uygulama verilerini yönetir.
- **Domain katmanı:** Birçok ekranda tekrar kullanılan karmaşık iş kuralları ortaya çıkarsa daha sonra eklenebilir.

V1'in yalnızca boş kütüphane ekranı bulunduğundan başlangıçta yapay repository veya domain sınıfları oluşturmayacağız.

## 10. Şimdilik eklemediğimiz teknolojiler

Aşağıdaki araçlar yararlı olabilir; ancak henüz ihtiyaç doğmadığı için ilk iskelete eklenmeyecektir:

- Room veritabanı
- Dependency Injection kütüphanesi
- Ağ/HTTP istemcisi
- Bulut servisi
- Kullanıcı hesabı
- Çok modüllü proje yapısı

Bu yaklaşım “bunları hiç kullanmayacağız” anlamına gelmez. İhtiyaç ortaya çıktığında ilgili problemi önce anlayacak, sonra uygun aracı ekleyeceğiz.

Özellikle ağ ve bulut bileşenleri PaperLit'in tamamen yerel çalışma kuralı nedeniyle zorunlu bağımlılığa dönüştürülmeyecektir.

## 11. Senin kontrol listen

Bu bölümden sonra şu soruları kendi cümlelerinle cevaplayabilmelisin:

1. Kotlin ile Jetpack Compose aynı şey midir?
2. Gradle ne işe yarar?
3. Gradle Wrapper neden repoya eklenir?
4. `app/src/main`, `app/src/test` ve `app/src/androidTest` arasındaki fark nedir?
5. Neden ilk günden çok modüllü mimari kurmuyoruz?
6. UI ve Data katmanlarının görevleri nelerdir?

Cevapların kusursuz olmak zorunda değil. Amaç ezber değil, proje ilerledikçe kavramların yerine oturmasıdır.

## 12. Bölümün tamamlanma ölçütü

Bu bölüm aşağıdaki koşullarda tamamlanmış sayılır:

- Kullanılacak temel teknolojiler belirlenmiştir.
- Tercihlerin nedenleri kaydedilmiştir.
- Başlangıç klasör yapısı anlaşılmıştır.
- İlk sürümde özellikle eklenmeyecek araçlar belirtilmiştir.
- Bir sonraki bölümde kurulacak proje iskeletinin sınırları nettir.

## 13. Sıradaki adım

Bir sonraki bölümde gerçek Android proje dosyalarını oluşturacağız. Önce en küçük uygulamayı derleyecek, ardından PaperLit başlığı ve “Kütüphanem boş” durumunu göstereceğiz.

## Resmî kaynaklar

- [Kotlin ve Android](https://developer.android.com/kotlin)
- [Jetpack Compose](https://developer.android.com/compose)
- [Android uygulama mimarisi](https://developer.android.com/topic/architecture)
- [Android Gradle Plugin hakkında](https://developer.android.com/build/releases/about-agp)
- [Android Gradle Plugin 9.3 uyumluluğu](https://developer.android.com/build/releases/agp-9-3-0-release-notes)
