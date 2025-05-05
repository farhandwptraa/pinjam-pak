package id.pinjampak.pinjam_pak.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleFeatureDTO {
    private UUID roleId;
    private UUID featureId;
    private boolean enabled;
}