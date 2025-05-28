package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private List<City> cities = new ArrayList<>();
}
