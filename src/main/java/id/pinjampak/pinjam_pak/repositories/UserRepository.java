package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByRole_NamaRoleIgnoreCase(String namaRole);
    boolean existsByUsername(String username); // Cek apakah username sudah ada
    boolean existsByEmail(String email); // Cek apakah email sudah ada
}
