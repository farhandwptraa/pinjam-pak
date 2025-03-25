package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "employeee")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id_user;

    @Column(nullable = false, unique = true)
    private int nip;

    @Column(nullable = false)
    private int id_role;

    @Column(nullable = false)
    private String branch;
}