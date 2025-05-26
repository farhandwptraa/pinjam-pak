package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.*;
import id.pinjampak.pinjam_pak.models.Pengajuan;
import id.pinjampak.pinjam_pak.services.PengajuanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pengajuan")
@RequiredArgsConstructor
public class PengajuanController {

    private final PengajuanService pengajuanService;

    @PostMapping
    public ResponseEntity<?> buatPengajuan(@RequestBody CreatePengajuanRequestDTO request,
                                           Principal principal) {
        pengajuanService.buatPengajuan(request, principal.getName());
        return ResponseEntity.ok("Pengajuan berhasil dibuat");
    }

    @PutMapping("/{id}/review-marketing")
    public ResponseEntity<?> reviewPengajuanOlehMarketing(@PathVariable UUID id,
                                                          @RequestBody MarketingReviewRequestDTO request,
                                                          Principal principal) {
        pengajuanService.reviewOlehMarketing(id, request, principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Review marketing berhasil diproses");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PengajuanPendingResponseDTO>> getPengajuanPendingUntukMarketing(Principal principal) {
        List<PengajuanPendingResponseDTO> list = pengajuanService.getPengajuanPendingUntukMarketing(principal.getName());
        return ResponseEntity.ok(list);
    }


    @PutMapping("/{id}/review-manager")
    public ResponseEntity<?> reviewByManager(
            @PathVariable UUID id,
            @RequestBody ReviewManagerRequestDTO request,
            Principal principal) {

        pengajuanService.reviewByBranchManager(
                id,
                principal.getName(),
                request.isDisetujui(),
                request.getCatatan()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Review manager berhasil diproses");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending-manager")
    public ResponseEntity<List<PengajuanPendingResponseDTO>> getPengajuanPendingUntukManager(Principal principal) {
        List<PengajuanPendingResponseDTO> list = pengajuanService.getPengajuanPendingUntukManager(principal.getName());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/disburse")
    public ResponseEntity<?> disbursePengajuan(
            @PathVariable UUID id,
            Principal principal) {

        pengajuanService.disbursePengajuan(id, principal.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Pengajuan berhasil dicairkan");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending-backoffice")
    public ResponseEntity<List<PengajuanPendingResponseDTO>> getPengajuanPendingUntukBackoffice(Principal principal) {
        List<PengajuanPendingResponseDTO> list = pengajuanService.getPengajuanPendingUntukBackoffice(principal.getName());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/semua")
    public ResponseEntity<List<PengajuanListResponseDTO>> getAllPengajuan(Principal principal) {
        List<PengajuanListResponseDTO> list = pengajuanService.getAllPengajuan(principal.getName());
        return ResponseEntity.ok(list);
    }
}
