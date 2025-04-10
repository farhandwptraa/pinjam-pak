package id.pinjampak.pinjam_pak.helper;

import id.pinjampak.pinjam_pak.enums.ProvinceArea;

import java.util.Set;

public class ProvinceAreaMapper {

    public static ProvinceArea getAreaByProvince(String province) {
        Set<String> area1 = Set.of("Aceh", "Sumatera Utara", "Sumatera Barat", "Riau",
                "Kepulauan Riau", "Jambi", "Bengkulu", "Sumatera Selatan", "Bangka Belitung",
                "Lampung", "DKI Jakarta", "Banten", "Jawa Barat");

        Set<String> area2 = Set.of("Jawa Tengah", "DI Yogyakarta", "Jawa Timur", "Bali",
                "Nusa Tenggara Barat", "Nusa Tenggara Timur", "Kalimantan Barat",
                "Kalimantan Tengah", "Kalimantan Selatan", "Kalimantan Timur", "Kalimantan Utara");

        Set<String> area3 = Set.of("Sulawesi Utara", "Sulawesi Tengah", "Sulawesi Selatan",
                "Sulawesi Tenggara", "Gorontalo", "Sulawesi Barat", "Maluku", "Maluku Utara",
                "Papua", "Papua Barat", "Papua Pegunungan", "Papua Tengah",
                "Papua Selatan", "Papua Barat Daya");

        if (area1.contains(province)) return ProvinceArea.AREA_1;
        if (area2.contains(province)) return ProvinceArea.AREA_2;
        if (area3.contains(province)) return ProvinceArea.AREA_3;

        throw new IllegalArgumentException("Provinsi tidak dikenali: " + province);
    }
}