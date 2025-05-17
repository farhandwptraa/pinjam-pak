package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "pinjaman")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Pinjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_pinjaman;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String status; // misalnya: AKTIF, LUNAS

    @Column(name = "tanggal_pencairan")
    private LocalDateTime tanggalPencairan;

    @Column(name = "bunga")
    private double bunga;
}
