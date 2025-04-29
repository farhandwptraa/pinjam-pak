package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PengajuanListResponseDTO {
    private UUID pengajuanId;
    private String namaCustomer;
    private Integer amount;
    private String status;
    private LocalDateTime tanggalPengajuan;
    private String namaMarketing;
}