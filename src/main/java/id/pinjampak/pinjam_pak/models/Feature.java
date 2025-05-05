package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "feature")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "feature_id")
    private UUID featureId;

    @Column(nullable = false)
    private String namaFeature;
}
