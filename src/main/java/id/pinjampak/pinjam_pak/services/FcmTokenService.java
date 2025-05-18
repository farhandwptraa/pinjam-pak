package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.models.FcmToken;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.FcmTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    public FcmTokenService(FcmTokenRepository fcmTokenRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
    }

    public void saveToken(User user, String token) {
        Optional<FcmToken> existing = fcmTokenRepository.findByToken(token);
        if (existing.isEmpty()) {
            FcmToken fcmToken = new FcmToken();
            fcmToken.setUser(user);
            fcmToken.setToken(token);
            fcmTokenRepository.save(fcmToken);
        }
    }

    @Transactional
    public void deleteToken(String token) {
        fcmTokenRepository.deleteByToken(token);
    }
}