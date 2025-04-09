package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.ForgotPasswordRequestDTO;
import id.pinjampak.pinjam_pak.models.PasswordResetToken;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.PasswordResetTokenRepository;
import id.pinjampak.pinjam_pak.repositories.UserRepository;
import id.pinjampak.pinjam_pak.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Autowired
    public ForgotPasswordController(UserRepository userRepository,
                                    PasswordResetTokenRepository tokenRepository,
                                    EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        String email = request.getEmail();

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Generate token
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);
            PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);

            tokenRepository.save(resetToken);

            // Kirim email reset password
            String resetLink = "http://localhost:4200/reset-password?token=" + token;
            emailService.sendResetPasswordEmail(email, resetLink);

            System.out.println("Reset link: " + resetLink);
        } else {
            System.out.println("User tidak ditemukan untuk email: " + email);
        }

        return ResponseEntity.ok("Jika email terdaftar, link reset akan dikirim.");
    }
}