# PaperLit Eğitim Rehberi

Bu klasör, PaperLit Android uygulamasını geliştirirken izlenen öğretici sürecin kalıcı kaydıdır.

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

Yeni geliştirme aşamaları başladıkça bu listeye yeni bölümler eklenecektir.

## Temel proje kuralları

- Uygulama telefonda yerel çalışır.
- Kullanıcı verileri cihazda tutulur.
- Uygulamanın çalışması için hosting gerekmez.
- APK, GitHub Actions üzerinden üretilebilir.
- `main` yalnızca kabul edilmiş ve çalışan sürümleri taşır.
- Geliştirmeler `feature/...` dallarında yapılır.
- Değişiklikler test edilir ve Pull Request üzerinden incelenir.
- Her aşamanın sonunda mümkün olduğunca çalışan, telefona kurulabilir bir APK bulunur.
