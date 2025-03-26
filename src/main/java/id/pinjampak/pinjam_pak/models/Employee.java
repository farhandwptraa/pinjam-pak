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
    private UUID employee_id;

    @Column(nullable = false, unique = true)
    private Long nip;

    @Column(nullable = false)
    private UUID branch_id;

    @Column(nullable = false)
    private UUID user_id;
}