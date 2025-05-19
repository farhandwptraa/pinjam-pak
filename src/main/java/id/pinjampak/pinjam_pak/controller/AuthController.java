package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.*;
import id.pinjampak.pinjam_pak.services.AuthService;
import id.pinjampak.pinjam_pak.services.FcmTokenService;
import id.pinjampak.pinjam_pak.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody(required = false) LogoutRequestDTO request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token tidak valid atau tidak ada.");
        }
        String token = authHeader.substring(7);
        authService.logout(token, request != null ? request.getFcmToken() : null);
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

    @PostMapping("/login-google")
    public ResponseEntity<AuthResponseDTO> loginWithGoogle(@RequestBody LoginWithGoogleDTO request) {
        return ResponseEntity.ok(authService.loginWithGoogle(request));
    }
}