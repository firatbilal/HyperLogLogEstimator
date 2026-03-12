public class Main {
    public static void main(String[] args) {
        System.out.println("--- HyperLogLog Estimator Test Başlıyor ---\n");

        int b = 14; // 16384 kova, beklenen teorik hata: %0.81
        HyperLogLogEstimator hll = new HyperLogLogEstimator(b);

        int actualCount = 1_000_000;
        System.out.println("1. AŞAMA: 1 Milyon Tekil Eleman Ekleme Testi");
        System.out.println("Veriler ekleniyor... Lütfen bekleyin.");

        for (int i = 0; i < actualCount; i++) {
            hll.add("veri_no_" + i);
        }

        long estimate = hll.estimate();
        double errorRate = Math.abs((double) (estimate - actualCount) / actualCount) * 100;

        System.out.println("Gerçek Eleman Sayısı : " + actualCount);
        System.out.println("HLL Tahmini          : " + estimate);
        System.out.printf("Gerçekleşen Hata Payı: %.2f%%\n\n", errorRate);

        System.out.println("--------------------------------------------------");

        System.out.println("2. AŞAMA: Birleştirilebilirlik (Merge) Testi");

        HyperLogLogEstimator hllA = new HyperLogLogEstimator(b);
        HyperLogLogEstimator hllB = new HyperLogLogEstimator(b);

        for (int i = 0; i < 500_000; i++) {
            hllA.add("A_serisi_" + i);
        }

        for (int i = 0; i < 500_000; i++) {
            hllB.add("B_serisi_" + i);
        }

        System.out.println("hllA tahmini (Beklenen ~500.000): " + hllA.estimate());
        System.out.println("hllB tahmini (Beklenen ~500.000): " + hllB.estimate());

        hllA.merge(hllB);

        long mergedEstimate = hllA.estimate();
        System.out.println("\nBirleştirme işlemi (Merge) yapıldı.");
        System.out.println("Birleştirilmiş HLL Tahmini (Beklenen ~1.000.000): " + mergedEstimate);

        double mergeErrorRate = Math.abs((double) (mergedEstimate - 1_000_000) / 1_000_000) * 100;
        System.out.printf("Birleştirilmiş Hata Payı: %.2f%%\n", mergeErrorRate);
    }
}