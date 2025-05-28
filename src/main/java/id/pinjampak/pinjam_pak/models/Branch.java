package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "branches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "nama_cabang", nullable = false)
    private String namaCabang;

    @Column(nullable = false)
    private String alamat;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<Province> provinces = new ArrayList<>();
}