package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Optional<Province> findByName(String name);
    List<Province> findByBranchBranchId(UUID branchId);

}


