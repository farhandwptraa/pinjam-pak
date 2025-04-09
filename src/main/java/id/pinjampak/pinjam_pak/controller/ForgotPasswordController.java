package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.ForgotPasswordRequestDTO;
import id.pinjampak.pinjam_pak.services.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @Autowired
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        forgotPasswordService.processForgotPassword(request.getEmail());
        return ResponseEntity.ok("Jika email terdaftar, link reset akan dikirim.");
    }
}