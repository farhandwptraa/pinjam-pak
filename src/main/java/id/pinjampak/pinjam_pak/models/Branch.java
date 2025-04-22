package id.pinjampak.pinjam_pak.models;

import id.pinjampak.pinjam_pak.enums.ProvinceArea;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "branches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID branch_id;

    @Column(name = "nama_cabang", nullable = false)
    private String namaCabang;

    @Column(nullable = false)
    private String alamat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProvinceArea area; // 👈 Tambahkan ini
}