package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.AuthRequestDTO;
import id.pinjampak.pinjam_pak.dto.AuthResponseDTO;
import id.pinjampak.pinjam_pak.dto.ChangePasswordRequestDTO;
import id.pinjampak.pinjam_pak.services.AuthService;
import id.pinjampak.pinjam_pak.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token tidak valid atau tidak ada.");
        }

        String token = authHeader.substring(7); // Menghapus "Bearer " dari token
        authService.logout(token);

        return ResponseEntity.ok("Logout berhasil.");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequestDTO request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractidUser(token);

        authService.changePassword(username, request);
        return ResponseEntity.ok("Password berhasil diubah.");
    }
}