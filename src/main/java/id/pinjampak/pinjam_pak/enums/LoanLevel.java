package id.pinjampak.pinjam_pak.enums;

import java.util.Map;

public enum LoanLevel {
    LEVEL_1(0.2, 3, Map.of(
            1, 0.04,
            2, 0.045,
            3, 0.05
    )),
    LEVEL_2(0.4, 6, Map.of(
            1, 0.035,
            2, 0.04,
            3, 0.045,
            6, 0.055
    )),
    LEVEL_3(0.7, 12, Map.of(
            1, 0.03,
            2, 0.035,
            3, 0.04,
            6, 0.05,
            12, 0.06
    )),
    LEVEL_4(1.0, 24, Map.of(
            1, 0.025,
            2, 0.03,
            3, 0.035,
            6, 0.045,
            12, 0.055,
            24, 0.065
    ));

    private final double plafondMultiplier;
    private final int maxTenor;
    private final Map<Integer, Double> bungaPerTenor;

    LoanLevel(double plafondMultiplier, int maxTenor, Map<Integer, Double> bungaPerTenor) {
        this.plafondMultiplier = plafondMultiplier;
        this.maxTenor = maxTenor;
        this.bungaPerTenor = bungaPerTenor;
    }

    public double getPlafondMultiplier() {
        return plafondMultiplier;
    }

    public int getMaxTenor() {
        return maxTenor;
    }

    public double getBungaByTenor(int tenor) {
        return bungaPerTenor.getOrDefault(tenor, bungaPerTenor.get(maxTenor));
    }
}