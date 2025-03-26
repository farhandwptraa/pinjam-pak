package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "role_feature")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RoleFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID role_feature_id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;
}