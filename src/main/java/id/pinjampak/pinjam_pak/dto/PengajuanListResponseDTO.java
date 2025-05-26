package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PengajuanListResponseDTO {
    private UUID pengajuanId;
    private String namaCustomer;
    private String amount;               // misal: formatted "1000000"
    private String status;
    private String tanggalPengajuan;     // misal: "2025-05-26T14:30:00"
    private String namaMarketing;
}