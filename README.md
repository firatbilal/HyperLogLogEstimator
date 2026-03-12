# HyperLogLog Estimator

Bu proje, büyük veri setlerinde bellek sınırlarını aşmadan, matematiksel ve olasılıksal yöntemlerle "Cardinality Estimation" (Küme Büyüklüğü Tahmini / Benzersiz Eleman Sayma) problemini çözmek amacıyla sıfırdan geliştirilmiş bir **HyperLogLog (HLL)** algoritması gerçeklemesidir. Proje, modern yazılım geliştirme pratiklerinden biri olan Agentic Kodlama yaklaşımı ile tasarlanmıştır.

## Özellikler

- **Yüksek Kaliteli Hash Fonksiyonu:** Verilerin kovalara (buckets) homojen dağılımını sağlamak için 32-bit `MurmurHash3` algoritması sıfırdan implemente edilmiştir.
- **Kovalama (Bucketing) ve Rank Takibi:** Gelen veriler, bit düzeyinde (bitwise) işlemlerle işlenerek maksimum bellek verimliliği ($O(1)$ işlem süresi) elde edilmiştir.
- **Harmonik Ortalama ve Düzeltme Faktörleri:** Standart HLL harmonik ortalama formülüne ek olarak, aykırı değerleri dengelemek için Küçük Değer (Linear Counting) ve Büyük Değer sapma düzeltmeleri koda entegre edilmiştir.
- **Kayıpsız Birleştirilebilirlik (Merge):** İki farklı HLL veri yapısı, aynı indeksteki maksimum rank değerleri üzerinden hiçbir veri kaybı yaşanmadan tek bir yapıda (distributed counting) birleştirilebilmektedir.

## Teorik Analiz ve Bellek Karmaşıklığı

Algoritmanın hata payı doğrudan kova sayısına ($m$) bağlıdır ve standart hata (Standard Error) formülü şu şekildedir:
**SE ≈ 1.04 / √m**

Bu projede test simülasyonları `b = 14` (16.384 kova) parametresi ile yapılmıştır. 
- **Teorik Hata Payı:** ~%0.81
- **Bellek Kullanımı:** Yalnızca ~16 KB. 

*Not: Geleneksel bir veri yapısıyla (örn. `HashSet`) milyonlarca benzersiz veriyi tutmak gigabaytlarca bellek gerektirirken, bu olasılıksal yapı sayesinde çok küçük bir bellek ayak iziyle yüksek doğrulukta tahmin yapılabilmektedir.*

## Nasıl Çalıştırılır?

Projeyi lokal ortamınızda derlemek ve 1 milyon tekil eleman ekleme ile birleştirme (merge) test senaryolarını çalıştırmak için terminalinizde şu komutları kullanabilirsiniz:

```bash
# Sınıfları derleyin
javac Main.java HyperLogLogEstimator.java

# Test senaryosunu çalıştırın
java Main
