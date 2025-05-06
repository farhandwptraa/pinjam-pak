package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.RoleFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleFeatureRepository extends JpaRepository<RoleFeature, UUID> {
    List<RoleFeature> findByRole_RoleId(UUID roleId);
    void deleteByRole_RoleId(UUID roleId);
    boolean existsByRole_RoleIdAndFeature_FeatureId(UUID roleId, UUID featureId);
}
