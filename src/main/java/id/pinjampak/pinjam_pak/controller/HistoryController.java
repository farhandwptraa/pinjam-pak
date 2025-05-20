package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.PengajuanResponseDTO;
import id.pinjampak.pinjam_pak.dto.PinjamanResponseDTO;
import id.pinjampak.pinjam_pak.models.Pengajuan;
import id.pinjampak.pinjam_pak.models.Pinjaman;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.security.CustomUserDetails;
import id.pinjampak.pinjam_pak.services.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/pengajuan")
    public ResponseEntity<List<PengajuanResponseDTO>> getPengajuanHistory(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(historyService.getPengajuanByUser(user));
    }

    @GetMapping("/pinjaman")
    public ResponseEntity<List<PinjamanResponseDTO>> getPinjamanHistory(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(historyService.getPinjamanByUser(user));
    }
}
