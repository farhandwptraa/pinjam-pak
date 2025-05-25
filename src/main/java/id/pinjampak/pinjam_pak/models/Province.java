package id.pinjampak.pinjam_pak.models;

import id.pinjampak.pinjam_pak.enums.ProvinceArea;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProvinceArea area;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private List<City> cities = new ArrayList<>();
}
