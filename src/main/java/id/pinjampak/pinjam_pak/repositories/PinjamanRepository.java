package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Pinjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PinjamanRepository extends JpaRepository<Pinjaman, UUID> {
}
