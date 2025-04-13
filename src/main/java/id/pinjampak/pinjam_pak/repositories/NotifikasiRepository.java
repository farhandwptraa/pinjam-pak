package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Notifikasi;
import id.pinjampak.pinjam_pak.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotifikasiRepository extends JpaRepository<Notifikasi, UUID> {
    List<Notifikasi> findByUserOrderByWaktuDibuatDesc(User user);
}