package id.pinjampak.pinjam_pak.dto;

import lombok.*;

@Getter @Setter
public class CreatePengajuanRequestDTO {
    private int amount;
    private Integer tenor; // dalam bulan, misalnya 12
    private String lokasi;
}

