package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "branch")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_cabang;

    @Column(nullable = false)
    private String nama_branch;
}