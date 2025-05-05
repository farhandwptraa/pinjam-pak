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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        String identifier = request.getUsernameOrEmail();
        String password = request.getPassword();

        // Coba cari user berdasarkan email atau username
        User user = identifier.contains("@")
                ? userRepository.findByEmail(identifier).orElseThrow(() -> new UsernameNotFoundException("User dengan email tidak ditemukan"))
                : userRepository.findByUsername(identifier).orElseThrow(() -> new UsernameNotFoundException("User dengan username tidak ditemukan"));

        // Autentikasi manual karena kita ambil user secara eksplisit
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password salah.");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        // Ambil role dari user (misalnya "ADMIN", "USER", dll.)
        String role = user.getRole().getNamaRole();  // Sesuaikan dengan nama role yang Anda gunakan
        String role_id = user.getRole().getRoleId().toString();  // Role ID

        return new AuthResponseDTO(token, role_id, user.getUsername(), role);  // Return dengan username dan role
    }

    public void logout(String token) {
        Date expiryDate = jwtUtil.extractExpiration(token); // ✅ Ambil expiry dari token

        // ✅ Konversi Date → LocalDateTime
        LocalDateTime localExpiryDate = expiryDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // ✅ Simpan token ke database
        BlacklistedToken blacklistedToken = new BlacklistedToken(token, localExpiryDate);
        blacklistedTokenRepository.save(blacklistedToken);
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