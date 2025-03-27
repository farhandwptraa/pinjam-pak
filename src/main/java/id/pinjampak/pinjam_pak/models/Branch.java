package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "branch")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID branch_id;

    @Column(nullable = false)
    private String nama_branch;
}