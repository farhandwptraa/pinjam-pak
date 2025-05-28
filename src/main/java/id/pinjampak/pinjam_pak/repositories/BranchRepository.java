package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
    Optional<Branch> findByNamaCabang(String namaCabang);
}
