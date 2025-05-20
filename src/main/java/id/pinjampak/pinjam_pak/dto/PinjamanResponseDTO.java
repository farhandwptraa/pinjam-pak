package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinjamanResponseDTO {
    private UUID idPinjaman;
    private int amount;
    private String status;
    private LocalDateTime tanggalPencairan;
    private double bunga;
}
