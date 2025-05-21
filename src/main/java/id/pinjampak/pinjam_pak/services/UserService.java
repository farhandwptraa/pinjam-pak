package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.models.Role;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.RoleRepository;
import id.pinjampak.pinjam_pak.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import id.pinjampak.pinjam_pak.dto.UserResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public List<UserResponseDTO> getAllUsersDTO() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getNama_lengkap(),
                user.getRole().getNamaRole()
        )).toList();
    }

    public Optional<UserResponseDTO> getUserDTOByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getNama_lengkap(),
                        user.getRole().getNamaRole()
                ));
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username sudah digunakan!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email sudah digunakan!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role defaultRole = roleRepository.findByNamaRole("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role Customer tidak ditemukan"));

        if (user.getRole() == null) {
            user.setRole(defaultRole);
        }

        if ("CUSTOMER".equalsIgnoreCase(user.getRole().getNamaRole())) {
            String token = UUID.randomUUID().toString();
            user.setVerificationToken(token);
            user.setEmailVerified(false);

            // Kirim email
            String link = "pinjampak://email-verification?token=" + token;
            String html = "<h3>Verifikasi Akun Anda</h3>"
                    + "<p>Klik link berikut untuk verifikasi akun Anda:</p>"
                    + "<a href=\"" + link + "\">Verifikasi Sekarang</a>"
                    + "<p>Terima kasih telah mendaftar di PinjamPak.</p>";
            emailService.sendHtmlEmail(user.getEmail(), "Verifikasi Akun Anda", html);
        } else {
            user.setEmailVerified(true); // Auto-verified untuk non-customer
        }

        return userRepository.save(user);
    }

    public User updateUser(UUID id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }).orElse(null);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> verifyUserByToken(String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }
}