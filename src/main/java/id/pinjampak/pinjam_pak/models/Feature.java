package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "feature")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_feature;

    @Column(nullable = false)
    private String nama_feature;
}