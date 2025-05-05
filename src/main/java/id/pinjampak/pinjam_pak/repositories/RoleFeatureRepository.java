package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.RoleFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleFeatureRepository extends JpaRepository<RoleFeature, UUID> {
    Optional<RoleFeature> findByRoleRoleIdAndFeatureFeatureId(UUID roleId, UUID featureId);
    List<RoleFeature> findByRoleRoleId(UUID roleId);
    boolean existsByRole_RoleIdAndFeature_FeatureId(UUID roleId, UUID featureId);
}