package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.RegisterRequestDTO;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.pinjampak.pinjam_pak.dto.UserResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsersDTO();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return userService.getUserDTOByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody RegisterRequestDTO dto) {
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());
        newUser.setNama_lengkap(dto.getNamaLengkap());

        User createdUser = userService.createUser(newUser);

        UserResponseDTO responseDTO = new UserResponseDTO(
                createdUser.getUserId(),
                createdUser.getUsername(),
                createdUser.getEmail(),
                createdUser.getNama_lengkap(),
                createdUser.getRole().getNamaRole()
        );

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        Optional<User> userOpt = userService.verifyUserByToken(token);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(Map.of("message", "Email berhasil diverifikasi."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Token tidak valid."));
        }
    }
}
