package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.models.PasswordResetToken;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.PasswordResetTokenRepository;
import id.pinjampak.pinjam_pak.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Autowired
    public ForgotPasswordService(UserRepository userRepository,
                                 PasswordResetTokenRepository tokenRepository,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public void processForgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Hapus token lama jika ada
            tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

            // Generate token baru
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);
            PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
            tokenRepository.save(resetToken);

            // Kirim email reset password dalam format HTML
            String resetLink = "pinjampak://reset-password?token=" + token;
            String htmlContent = "<p>Klik link berikut untuk mereset password Anda:</p>"
                    + "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
                    + "<p>Link ini hanya berlaku selama 30 menit.</p>";

            emailService.sendHtmlEmail(email, "Reset Password", htmlContent);
            System.out.println("Reset link: " + resetLink);
        } else {
            System.out.println("User tidak ditemukan untuk email: " + email);
        }
    }
}