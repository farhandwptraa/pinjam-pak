package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "pengajuan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Pengajuan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_pengajuan;

    @Column(nullable = false)
    private UUID id_user;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private int tenor;

    @Column(nullable = false)
    private double angsuran;
}