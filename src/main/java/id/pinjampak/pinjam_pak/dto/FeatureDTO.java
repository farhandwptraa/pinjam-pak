package id.pinjampak.pinjam_pak.dto;

import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FeatureDTO {
    private UUID featureId;
    private String namaFeature;
}

