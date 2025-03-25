package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_user;

    @Column(nullable = false)
    private UUID id_customer;

    @Column(nullable = false)
    private String nama_lengkap;

    @Column(nullable = false)
    private String tempat_lahir;

    @Column(nullable = false)
    private String tanggal_lahir;

    @Column(nullable = false)
    private String pekerjaan;

    @Column(nullable = false)
    private int gaji;

    @Column(nullable = false)
    private String alamat;

    @Column(nullable = false)
    private String no_hp;

    @Column(nullable = false)
    private String nama_ibu_kandung;

    @Column(nullable = false)
    private double plafond;

}