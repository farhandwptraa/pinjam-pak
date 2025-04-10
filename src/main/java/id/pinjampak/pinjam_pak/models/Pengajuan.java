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
    @JoinColumn(name = "id_user", nullable = false)
    private User user; // user = customer

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
    private String status; // e.g. PENDING, REJECTED_MARKETING, APPROVED_MARKETING, REJECTED_MANAGER, APPROVED_MANAGER, DISBURSED

    @Column(name = "tanggal_pengajuan")
    private LocalDateTime tanggalPengajuan;

    @Column(name = "tanggal_disetujui_marketing")
    private LocalDateTime tanggalDisetujuiMarketing;

    @Column(name = "tanggal_disetujui_manager")
    private LocalDateTime tanggalDisetujuiManager;

    @Column(name = "tanggal_pencairan")
    private LocalDateTime tanggalPencairan;

    @Column(name = "catatan_marketing")
    private String catatanMarketing;

    @Column(name = "catatan_manager")
    private String catatanManager;
}

