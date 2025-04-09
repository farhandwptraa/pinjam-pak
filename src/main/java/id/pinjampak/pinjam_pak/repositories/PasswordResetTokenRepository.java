package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.PasswordResetToken;
import id.pinjampak.pinjam_pak.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
}