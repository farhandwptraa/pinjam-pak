package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.AuthRequestDTO;
import id.pinjampak.pinjam_pak.dto.AuthResponseDTO;
import id.pinjampak.pinjam_pak.dto.ChangePasswordRequestDTO;
import id.pinjampak.pinjam_pak.models.BlacklistedToken;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.BlacklistedTokenRepository;
import id.pinjampak.pinjam_pak.repositories.UserRepository;
import id.pinjampak.pinjam_pak.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class AuthService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final FcmTokenService fcmTokenService;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, FcmTokenService fcmTokenService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.fcmTokenService = fcmTokenService;
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        String identifier = request.getUsernameOrEmail();
        String password = request.getPassword();

        System.out.println("Login request: " + request.getUsernameOrEmail() + ", FCM: " + request.getFcmToken());

        User user = identifier.contains("@")
                ? userRepository.findByEmail(identifier).orElseThrow(() -> new UsernameNotFoundException("User dengan email tidak ditemukan"))
                : userRepository.findByUsername(identifier).orElseThrow(() -> new UsernameNotFoundException("User dengan username tidak ditemukan"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password salah.");
        }
        if (request.getFcmToken() != null && user.getRole().getNamaRole().equalsIgnoreCase("CUSTOMER")) {
            fcmTokenService.saveToken(user, request.getFcmToken());
        }


        String token = jwtUtil.generateToken(user.getUsername());
        String role = user.getRole().getNamaRole();
        String role_id = user.getRole().getRoleId().toString();

        // ✅ Ambil customer ID jika ada
        String customerId = null;
        if (user.getCustomer() != null) {
            customerId = user.getCustomer().getCustomer_id().toString();
        }

        return new AuthResponseDTO(token, role_id, user.getUsername(), role, customerId);
    }


    public void logout(String token, String fcmToken) {
        Date expiryDate = jwtUtil.extractExpiration(token);
        LocalDateTime localExpiryDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        blacklistedTokenRepository.save(new BlacklistedToken(token, localExpiryDate));

        String username = jwtUtil.extractidUser(token);
        userRepository.findByUsername(username).ifPresent(user -> {
            if (fcmToken != null) fcmTokenService.deleteToken(fcmToken);
        });
    }

    public void changePassword(String username, ChangePasswordRequestDTO request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Password lama tidak sesuai.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}