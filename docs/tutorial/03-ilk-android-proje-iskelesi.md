# 03 — İlk Android Proje İskeleti

## 1. Bu bölümde ne yaptık?

PaperLit reposuna gerçek bir Android uygulama modülü eklendi. Mevcut `streamlit_app.py` ve `requirements.txt` dosyaları değiştirilmedi; Android uygulaması ayrı `app/` klasöründe oluşturuldu.

İlk iskelet:

- Kotlin ile yazılmıştır.
- Jetpack Compose ile ekran oluşturur.
- PaperLit başlığını gösterir.
- “Kütüphanem boş” başlangıç durumunu gösterir.
- `0.1.0` sürüm bilgisini gösterir.
- İki yerel birim testi içerir.
- Bir Compose arayüz testi içerir.
- GitHub Actions üzerinden test ve APK üretme iş akışı içerir.

## 2. Üç aracın gerçek dosyalardaki karşılığı

Önceki bölümde yaptığımız ayrım artık gerçek proje üzerinde görülebilir:

| Araç | Projedeki örnek |
|---|---|
| Kotlin | `MainActivity.kt`, `PaperLitApp.kt`, `LibraryUiState.kt` |
| Jetpack Compose | `PaperLitApp()` içindeki `Text`, `Column`, `Scaffold` bileşenleri |
| Gradle | `build.gradle.kts`, `app/build.gradle.kts`, Gradle Wrapper dosyaları |

Kotlin kodu yazarız. Compose ile ekranı tarif ederiz. Gradle ise gerekli araçları ve kütüphaneleri kullanarak test ve APK üretim sürecini yönetir.

## 3. Paket ve uygulama kimliği

İlk Android paket adı ve uygulama kimliği şudur:

```text
com.farukdemirel.paperlit
```

Bu kimlik Android işletim sisteminin uygulamayı diğer uygulamalardan ayırmasını sağlar. Kullanıcının ekranda gördüğü `PaperLit` adıyla aynı kavram değildir.

## 4. `MainActivity.kt`

Android uygulaması açıldığında çalışan başlangıç sınıfıdır:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaperLitApp()
        }
    }
}
```

Akış:

1. Android `MainActivity` sınıfını açar.
2. `onCreate()` çağrılır.
3. `setContent` Compose arayüzünü başlatır.
4. `PaperLitApp()` ekrana ne çizileceğini tarif eder.

## 5. `LibraryUiState.kt`

Bu sınıf ekranda gösterilecek küçük veri durumunu temsil eder:

```kotlin
data class LibraryUiState(
    val documentCount: Int = 0,
)
```

Henüz gerçek bir veritabanımız yoktur. Varsayılan belge sayısı sıfırdır. Bu nedenle `statusText` değeri “Kütüphanem boş” olur.

Ekran metnini doğrudan arayüzün içine gömmek yerine küçük bir durum sınıfında hesaplamamız iki avantaj sağlar:

- İş kuralını ekran kodundan ayırmaya başlarız.
- Android cihazı olmadan birim testi yazabiliriz.

## 6. `PaperLitApp.kt`

Bu dosya ilk Compose ekranını içerir.

Önemli Compose kavramları:

- `@Composable`: Fonksiyonun bir arayüz parçası tarif ettiğini belirtir.
- `MaterialTheme`: Material 3 görünüm değerlerini sağlar.
- `Scaffold`: Temel ekran yerleşimi için çatı sunar.
- `Column`: Öğeleri dikey sırada yerleştirir.
- `Text`: Ekranda metin gösterir.
- `Modifier`: Boyut, boşluk ve yerleşim gibi davranışları tanımlar.
- `@Preview`: Android Studio içinde ekran önizlemesi sağlar.

## 7. Birim testi

`LibraryUiStateTest.kt`, Android telefon çalıştırmadan durum sınıfını kontrol eder.

Test edilen iki senaryo:

1. Belge sayısı sıfırsa “Kütüphanem boş” yazması
2. Belge sayısı üçse “3 belge” yazması

Bir testin genel yapısı şöyledir:

```text
Hazırla → Çalıştır → Sonucu doğrula
```

Örneğimizde:

```text
LibraryUiState oluştur → statusText değerini oku → beklenen metinle karşılaştır
```

## 8. Arayüz testi

`PaperLitAppTest.kt`, Compose ekranında şu metinlerin gerçekten görüntülendiğini kontrol eder:

- `PaperLit`
- `Kütüphanem boş`
- `Sürüm 0.1.0`

Bu test `app/src/androidTest/` altındadır; çünkü Android çalışma ortamına ihtiyaç duyar. GitHub Actions'ın ilk adımında yalnız hızlı yerel birim testleri ve APK derlemesi çalıştırılacaktır. Emülatör üzerinde arayüz testi çalıştırma ayrı bir aşamada eklenecektir.

## 9. GitHub Actions iş akışı

`.github/workflows/android.yml` şu adımları uygular:

1. Repodaki kaynak kodu çalışma makinesine alır.
2. JDK 17'yi kurar.
3. Gradle önbelleğini ve çalışma ortamını hazırlar.
4. Gradle Wrapper dosyasını güvenlik açısından doğrular.
5. Yerel birim testlerini çalıştırır.
6. Debug APK üretir.
7. APK'yı indirilebilir GitHub Actions artifact'i olarak yükler.

GitHub Actions ekranındaki yeşil onay yalnız “dosya GitHub'a yüklendi” anlamına gelmez. Testlerin ve gerçek Android derlemesinin başarılı olduğunu gösterir.

## 10. Yerel doğrulama durumu

Bu iskelet hazırlanırken Gradle Wrapper JAR dosyasının SHA-256 özeti kontrol edildi:

```text
497c8c2a7e5031f6aa847f88104aa80a93532ec32ee17bdb8d1d2f67a194a9c7
```

Hazırlama ortamında `services.gradle.org` alan adına ağ erişimi bulunmadığı için Gradle dağıtımı yerel olarak indirilemedi. Bu nedenle yerel derleme sonucu henüz **PASS** olarak kabul edilmemiştir.

Gerçek doğrulama, dosyalar GitHub'a gönderildikten sonra GitHub Actions üzerinde yapılacaktır. İş akışı başarısız olursa hata mesajı incelenecek, neden açıklanacak ve düzeltme ayrı bir commit olarak uygulanacaktır.

## 11. Senin kontrol listen

Bu bölümden sonra şu soruları cevaplamaya çalış:

1. `MainActivity` ile `PaperLitApp` aynı görevi mi yapıyor?
2. `LibraryUiState` neden doğrudan ekran kodunun içine yazılmadı?
3. `src/test` ile `src/androidTest` arasındaki fark nedir?
4. GitHub Actions'taki başarılı yeşil işaret bize neyi kanıtlar?
5. `applicationId` ile ekranda görülen uygulama adı aynı şey midir?

## 12. Bölümün tamamlanma ölçütü

Bu bölüm ancak aşağıdaki sonuçlar alındığında tamamlanmış sayılır:

- Kaynak dosyalar `feature/android-v1` dalındadır.
- GitHub Actions birim testleri geçmiştir.
- Debug APK başarıyla üretilmiştir.
- APK artifact olarak indirilebilir durumdadır.
- APK gerçek Android telefona kurulup açılmıştır.

Son madde kullanıcı tarafından telefon üzerinde doğrulanacaktır.
