package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.FeatureDTO;
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

    public List<FeatureDTO> getFeaturesByRole(UUID roleId) {
        List<RoleFeature> roleFeatures = roleFeatureRepository.findByRole_RoleId(roleId);
        return roleFeatures.stream()
                .map(rf -> new FeatureDTO(rf.getFeature().getFeatureId(), rf.getFeature().getNamaFeature()))
                .toList();
    }

    public List<FeatureDTO> getAllFeatures() {
        return featureRepository.findAll().stream()
                .map(feature -> new FeatureDTO(feature.getFeatureId(), feature.getNamaFeature()))
                .toList();
    }

    @Transactional
    public void updateRoleFeatures(UUID roleId, List<UUID> featureIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Hapus fitur sebelumnya
        roleFeatureRepository.deleteByRole_RoleId(roleId);

        // Tambahkan fitur baru
        List<Feature> features = featureRepository.findAllById(featureIds);
        List<RoleFeature> roleFeatures = features.stream()
                .map(feature -> new RoleFeature(null, role, feature))
                .toList();

        roleFeatureRepository.saveAll(roleFeatures);
    }
}