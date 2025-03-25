package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "role")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_role;

    @Column(nullable = false)
    private String nama_role;
}