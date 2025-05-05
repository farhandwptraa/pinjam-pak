package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    boolean existsByNamaFeature(String namaFeature);
}