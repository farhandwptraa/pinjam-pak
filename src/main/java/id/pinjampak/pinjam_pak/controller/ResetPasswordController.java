package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.ResetPasswordRequestDTO;
import id.pinjampak.pinjam_pak.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        boolean success = resetPasswordService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok("Password berhasil direset.");
        } else {
            return ResponseEntity.badRequest().body("Token tidak valid atau sudah expired.");
        }
    }
}