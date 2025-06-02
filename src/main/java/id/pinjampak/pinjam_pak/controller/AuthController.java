package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.*;
import id.pinjampak.pinjam_pak.services.AuthService;
import id.pinjampak.pinjam_pak.services.FcmTokenService;
import id.pinjampak.pinjam_pak.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader,
                                    @RequestBody(required = false) LogoutRequestDTO request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", "Token tidak valid atau tidak ada.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String token = authHeader.substring(7);
        authService.logout(token, request != null ? request.getFcmToken() : null);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Logout berhasil.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequestDTO request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractidUser(token);

        authService.changePassword(username, request);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Password berhasil diubah.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login-google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody LoginWithGoogleDTO request) {
        try {
            AuthResponseDTO dto = authService.loginWithGoogle(request);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException ex) {
            Map<String,Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now().toString());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}