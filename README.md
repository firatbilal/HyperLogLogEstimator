# HyperLogLog Estimator

Bu proje, "Algoritma Tasarımı ve Analizi" dersi kapsamında Agentic Kodlama yaklaşımı kullanılarak sıfırdan geliştirilmiş bir **HyperLogLog (HLL)** algoritması gerçeklemesidir. 

Büyük veri setlerinde bellek sınırlarını aşmadan, matematiksel ve olasılıksal yöntemlerle "Cardinality Estimation" (Küme Büyüklüğü Tahmini) problemini çözer.

## Özellikler

- **Yüksek Kaliteli Hash Fonksiyonu:** Verilerin homojen dağılımı için 32-bit `MurmurHash3` algoritması sıfırdan implemente edilmiştir.
- **Kovalama (Bucketing) ve Rank Takibi:** Gelen veriler, bit düzeyinde (bitwise) işlemlerle işlenerek bellek verimliliği ($O(1)$ işlem süresi) sağlanmıştır.
- **Harmonik Ortalama ve Düzeltme Faktörleri:** Standart HLL harmonik ortalama formülüne ek olarak, Küçük Değer (Linear Counting) ve Büyük Değer düzeltmeleri koda entegre edilmiştir.
- **Kayıpsız Birleştirilebilirlik (Merge):** İki farklı HLL veri yapısı, maksimum rank değerleri üzerinden veri kaybı yaşanmadan tek bir yapıda birleştirilebilmektedir.

## Teorik Analiz ve Bellek Karmaşıklığı

Algoritmanın hata payı doğrudan kova sayısına ($m$) bağlıdır ve standart hata formülü şu şekildedir:
**SE ≈ 1.04 / √m**

Bu projede testler `b = 14` (16.384 kova) parametresi ile yapılmıştır. 
- **Teorik Hata Payı:** ~%0.81
- **Bellek Kullanımı:** Sadece ~16 KB. 
Geleneksel bir `HashSet` ile milyonlarca veriyi tutmak gigabaytlarca bellek gerektirirken, bu olasılıksal yapı sayesinde çok küçük bir bellek alanıyla yüksek doğrulukta tahmin yapılabilmektedir.

## Nasıl Çalıştırılır?

Projeyi derlemek ve test senaryosunu (1 milyon tekil eleman ekleme ve iki HLL yapısını birleştirme) çalıştırmak için:

```bash
javac Main.java HyperLogLogEstimator.java
java Main
