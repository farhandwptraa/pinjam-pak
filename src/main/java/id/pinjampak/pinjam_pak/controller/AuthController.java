package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.AuthRequestDTO;
import id.pinjampak.pinjam_pak.dto.AuthResponseDTO;
import id.pinjampak.pinjam_pak.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}