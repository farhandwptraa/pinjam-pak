package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PengajuanPendingResponseDTO {
    private String idPengajuan;
    private String namaCustomer;
    private Integer amount;
    private Integer tenor;
    private String status;
    private String tanggalPengajuan;
    private String catatanMarketing;
    private String catatanManager;
    private String lokasi;
    private Double amountFinal;
    private String namaMarketing;
}