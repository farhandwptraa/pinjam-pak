package id.pinjampak.pinjam_pak.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import id.pinjampak.pinjam_pak.dto.AuthRequestDTO;
import id.pinjampak.pinjam_pak.dto.AuthResponseDTO;
import id.pinjampak.pinjam_pak.dto.ChangePasswordRequestDTO;
import id.pinjampak.pinjam_pak.dto.LoginWithGoogleDTO;
import id.pinjampak.pinjam_pak.models.BlacklistedToken;
import id.pinjampak.pinjam_pak.models.Customer;
import id.pinjampak.pinjam_pak.models.Role;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.BlacklistedTokenRepository;
import id.pinjampak.pinjam_pak.repositories.RoleRepository;
import id.pinjampak.pinjam_pak.repositories.UserRepository;
import id.pinjampak.pinjam_pak.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final FcmTokenService fcmTokenService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, FcmTokenService fcmTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    @Transactional
    public void logout(String token, String fcmToken) {
        // Simpan token ke blacklist dengan expiry date
        Date expiryDate = jwtUtil.extractExpiration(token);
        LocalDateTime localExpiryDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        BlacklistedToken blacklistedToken = new BlacklistedToken(token, localExpiryDate);
        blacklistedTokenRepository.save(blacklistedToken);

        // Hapus FCM token jika ada
        String username = jwtUtil.extractidUser(token);
        userRepository.findByUsername(username).ifPresent(user -> {
            if (fcmToken != null) {
                fcmTokenService.deleteToken(fcmToken);
            }
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

    private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
            .Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList("29525165356-5ppm1frk6aldubkfum83ks1ok9knkoe2.apps.googleusercontent.com")) // ganti dengan client ID dari Firebase
            .build();

    public AuthResponseDTO loginWithGoogle(LoginWithGoogleDTO request) {
        try {
            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user = optionalUser.orElseGet(() -> {
                Role defaultRole = roleRepository.findByNamaRole("CUSTOMER")
                        .orElseThrow(() -> new RuntimeException("Role Customer tidak ditemukan"));

                               User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(email);
                newUser.setNama_lengkap(name);
                newUser.setPassword("");
                newUser.setRole(defaultRole);

                return userRepository.save(newUser);
            });

            if (request.getFcmToken() != null && user.getRole().getNamaRole().equalsIgnoreCase("CUSTOMER")) {
                fcmTokenService.saveToken(user, request.getFcmToken());
            }

            String token = jwtUtil.generateToken(user.getUsername());
            String role = user.getRole().getNamaRole();
            String roleId = user.getRole().getRoleId().toString();
            String customerId = user.getCustomer() != null
                    ? user.getCustomer().getCustomer_id().toString()
                    : null;

            return new AuthResponseDTO(token, roleId, user.getUsername(), role, customerId);
        } catch (Exception e) {
            throw new RuntimeException("Google login failed: " + e.getMessage());
        }
    }
}