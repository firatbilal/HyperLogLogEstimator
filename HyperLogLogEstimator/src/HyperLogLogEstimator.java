public class HyperLogLogEstimator {

    private final int b;
    private final int m;
    private final byte[] registers;

    // Constructor: Kova sayısını belirler ve diziyi başlatır
    public HyperLogLogEstimator(int b) {
        if (b < 4 || b > 16) {
            throw new IllegalArgumentException("b değeri genelde 4 ile 16 arasında olmalıdır.");
        }
        this.b = b;
        this.m = 1 << b;
        this.registers = new byte[m];
    }

    // Veriyi hash'leyip ilgili kovaya ekler
    public void add(String item) {
        int hash = murmurHash3(item);
        int index = hash >>> (32 - b);
        int w = hash << b;

        int rank = Integer.numberOfLeadingZeros(w) + 1;
        if (rank > 32 - b + 1) {
            rank = 32 - b + 1;
        }

        if (rank > registers[index]) {
            registers[index] = (byte) rank;
        }
    }

    // Harmonik ortalama ve düzeltme faktörleri ile tahmini hesaplar
    public long estimate() {
        double sum = 0.0;
        int zeroCount = 0;

        for (int i = 0; i < m; i++) {
            sum += 1.0 / (1 << registers[i]);
            if (registers[i] == 0) {
                zeroCount++;
            }
        }

        double alphaMM = getAlphaMM(m);
        double estimate = alphaMM / sum;

        // Düzeltme Faktörleri
        if (estimate <= (5.0 / 2.0) * m) {
            if (zeroCount > 0) {
                estimate = m * Math.log((double) m / zeroCount);
            }
        } else if (estimate > (1.0 / 30.0) * 4294967296.0) {
            estimate = -4294967296.0 * Math.log(1.0 - (estimate / 4294967296.0));
        }

        return Math.round(estimate);
    }

    // İki farklı HLL yapısını kayıpsız birleştirir
    public void merge(HyperLogLogEstimator other) {
        if (this.m != other.m) {
            throw new IllegalArgumentException("Birleştirilecek HLL yapılarının kova sayıları (m) aynı olmalıdır.");
        }

        for (int i = 0; i < this.m; i++) {
            if (other.registers[i] > this.registers[i]) {
                this.registers[i] = other.registers[i];
            }
        }
    }

    // m değerine göre alpha sabitini hesaplar
    private double getAlphaMM(int m) {
        double alpha;
        switch (m) {
            case 16:
                alpha = 0.673;
                break;
            case 32:
                alpha = 0.697;
                break;
            case 64:
                alpha = 0.709;
                break;
            default:
                alpha = 0.7213 / (1.0 + 1.079 / m);
                break;
        }
        return alpha * m * m;
    }

    // Yüksek kaliteli 32-bit MurmurHash3 implementasyonu
    private int murmurHash3(String input) {
        byte[] data = input.getBytes();
        int c1 = 0xcc9e2d51;
        int c2 = 0x1b873593;
        int h1 = 12345;

        int i = 0;
        while (i <= data.length - 4) {
            int k1 = (data[i] & 0xFF) | ((data[i+1] & 0xFF) << 8) |
                    ((data[i+2] & 0xFF) << 16) | ((data[i+3] & 0xFF) << 24);

            k1 *= c1;
            k1 = (k1 << 15) | (k1 >>> 17);
            k1 *= c2;

            h1 ^= k1;
            h1 = (h1 << 13) | (h1 >>> 19);
            h1 = h1 * 5 + 0xe6546b64;
            i += 4;
        }

        int k1 = 0;
        int tailLength = data.length - i;
        if (tailLength == 3) k1 ^= (data[i+2] & 0xFF) << 16;
        if (tailLength >= 2) k1 ^= (data[i+1] & 0xFF) << 8;
        if (tailLength >= 1) {
            k1 ^= (data[i] & 0xFF);
            k1 *= c1;
            k1 = (k1 << 15) | (k1 >>> 17);
            k1 *= c2;
            h1 ^= k1;
        }

        h1 ^= data.length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        return h1;
    }
}