package id.pinjampak.pinjam_pak.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerRequestDTO {
    private String nik;
    private String tempat_lahir;
    private Date tanggal_lahir;
    private String pekerjaan;
    private Long gaji;
    private String no_hp;
    private String nama_ibu_kandung;
    private String alamat;
    private String provinsi;
}