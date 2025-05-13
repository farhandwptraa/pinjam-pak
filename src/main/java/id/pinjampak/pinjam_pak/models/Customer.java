package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customer_id;

    @Column(nullable = false, unique = true)
    private String nik ;

    @Column(nullable = false)
    private String alamat;

    @Column(nullable = false)
    private String provinsi;

    @Column(nullable = false)
    private String tempat_lahir;

    @Column(nullable = false)
    private Date tanggal_lahir;

    @Column(nullable = false)
    private String pekerjaan;

    @Column(nullable = false)
    private Long gaji;

    @Column(nullable = false)
    private Double plafond;

    @Column(nullable = false)
    private Double sisa_plafond;

    @Column(nullable = false)
    private String no_hp;

    @Column(nullable = false)
    private String nama_ibu_kandung;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "foto_ktp_url")
    private String fotoKtpUrl;
}