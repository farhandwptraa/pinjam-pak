package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewManagerDTO {
    private UUID idPengajuan;
    private String namaCustomer;
    private int amount;
    private Integer tenor;
    private String status;
    private LocalDateTime tanggalPengajuan;
    private String catatanMarketing;
}
