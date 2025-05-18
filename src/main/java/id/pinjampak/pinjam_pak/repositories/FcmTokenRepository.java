package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.FcmToken;
import id.pinjampak.pinjam_pak.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FcmTokenRepository extends JpaRepository<FcmToken, UUID> {
    Optional<FcmToken> findByToken(String token);
    void deleteByToken(String token);
    List<FcmToken> findByUser(User user);
}