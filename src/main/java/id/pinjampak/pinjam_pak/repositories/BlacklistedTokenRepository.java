package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, UUID> {
    Optional<BlacklistedToken> findByToken(String token);
}