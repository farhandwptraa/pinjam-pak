package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.RoleFeatureDTO;
import id.pinjampak.pinjam_pak.models.*;
import id.pinjampak.pinjam_pak.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleFeatureService {

    private final RoleRepository roleRepository;
    private final FeatureRepository featureRepository;
    private final RoleFeatureRepository roleFeatureRepository;

    public Map<String, Object> getAllRoleFeatures() {
        List<Role> roles = roleRepository.findAll();
        List<Feature> features = featureRepository.findAll();
        List<RoleFeature> mappings = roleFeatureRepository.findAll();

        return Map.of(
                "roles", roles,
                "features", features,
                "mappings", mappings
        );
    }

    @Transactional
    public void updateRoleFeatures(List<RoleFeatureDTO> dtos) {
        for (RoleFeatureDTO dto : dtos) {
            Optional<RoleFeature> existing = roleFeatureRepository.findByRoleRoleIdAndFeatureFeatureId(dto.getRoleId(), dto.getFeatureId());

            if (dto.isEnabled()) {
                if (existing.isEmpty()) {
                    Role role = roleRepository.findById(dto.getRoleId()).orElseThrow();
                    Feature feature = featureRepository.findById(dto.getFeatureId()).orElseThrow();
                    roleFeatureRepository.save(new RoleFeature(null, role, feature));
                }
            } else {
                existing.ifPresent(roleFeatureRepository::delete);
            }
        }
    }

    public List<UUID> getFeatureIdsByRole(UUID roleId) {
        return roleFeatureRepository.findByRoleRoleId(roleId)
                .stream()
                .map(rf -> rf.getFeature().getFeatureId())
                .collect(Collectors.toList());
    }
}