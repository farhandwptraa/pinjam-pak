package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "role")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID role_id;

    @Column(name = "nama_role", nullable = false, unique = true)
    private String namaRole;
}