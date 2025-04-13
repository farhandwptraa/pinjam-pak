package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pengajuan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Pengajuan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_pengajuan;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // customer yang mengajukan

    @ManyToOne
    @JoinColumn(name = "marketing_id")
    private Employee marketing;

    @ManyToOne
    @JoinColumn(name = "branch_manager_id")
    private Employee branchManager;

    @ManyToOne
    @JoinColumn(name = "backoffice_id")
    private Employee backOffice;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private Integer tenor;

    @Column(nullable = false)
    private String status; // PENDING, REVIEWED, APPROVED, DISBURSED, REJECTED

    private LocalDateTime tanggalPengajuan;
    private LocalDateTime tanggalDisetujuiMarketing;
    private LocalDateTime tanggalDisetujuiManager;
    private LocalDateTime tanggalPencairan;

    private String catatanMarketing;
    private String catatanManager;
}