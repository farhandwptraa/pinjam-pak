package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "pinjaman")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Pinjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_pinjaman;

    @Column(nullable = false)
    private UUID id_user;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String status;
}