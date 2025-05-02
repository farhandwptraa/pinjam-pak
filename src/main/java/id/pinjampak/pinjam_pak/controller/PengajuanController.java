package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.*;
import id.pinjampak.pinjam_pak.models.Pengajuan;
import id.pinjampak.pinjam_pak.services.PengajuanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public ResponseEntity<?> reviewOlehMarketing(@PathVariable UUID id,
                                                          @RequestBody MarketingReviewRequestDTO request,
                                                          Principal principal) {
        pengajuanService.reviewOlehMarketing(id, request, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Review marketing berhasil diproses"));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReviewMarketingDTO>> getPengajuanPendingUntukMarketing(Principal principal) {
        List<ReviewMarketingDTO> list = pengajuanService.getPengajuanPendingUntukMarketing(principal.getName());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/review-manager")
    public ResponseEntity<?> reviewByManager(
            @PathVariable UUID id,
            @RequestBody ReviewManagerRequestDTO request,
            Principal principal) {

        pengajuanService.reviewByBranchManager(id, principal.getName(), request.isDisetujui(), request.getCatatan());

        return ResponseEntity.ok(Map.of("message", "Review manager berhasil diproses"));
    }

    @GetMapping("/pending-manager")
    public ResponseEntity<List<ReviewManagerDTO>> getPengajuanPendingUntukManager(Principal principal) {
        List<ReviewManagerDTO> list = pengajuanService.getPengajuanPendingUntukManager(principal.getName());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/disburse")
    public ResponseEntity<Void> disbursePengajuan(
            @PathVariable UUID id,
            Principal principal) {

        pengajuanService.disbursePengajuan(id, principal.getName());
        return ResponseEntity.ok().build(); // Tidak kirim body, hanya status 200 OK
    }

    @GetMapping("/pending-backoffice")
    public ResponseEntity<List<ReviewBackofficeDTO>> getPengajuanPendingUntukBackoffice(Principal principal) {
        List<ReviewBackofficeDTO> list = pengajuanService.getPengajuanPendingUntukBackoffice(principal.getName());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/semua")
    public ResponseEntity<List<PengajuanListResponseDTO>> getAllPengajuan(Principal principal) {
        List<PengajuanListResponseDTO> list = pengajuanService.getAllPengajuanByRole(principal.getName());
        return ResponseEntity.ok(list);
    }
}
