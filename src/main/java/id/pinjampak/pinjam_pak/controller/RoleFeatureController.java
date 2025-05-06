package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.FeatureDTO;
import id.pinjampak.pinjam_pak.services.RoleFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/role-feature")
@RequiredArgsConstructor
public class RoleFeatureController {

    private final RoleFeatureService roleFeatureService;

    @GetMapping("/{roleId}")
    public ResponseEntity<List<FeatureDTO>> getFeaturesByRole(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleFeatureService.getFeaturesByRole(roleId));
    }

    @GetMapping("/features")
    public ResponseEntity<List<FeatureDTO>> getAllFeatures() {
        return ResponseEntity.ok(roleFeatureService.getAllFeatures());
    }

    @PostMapping("/{roleId}")
    public ResponseEntity<Void> updateRoleFeatures(@PathVariable UUID roleId, @RequestBody List<UUID> featureIds) {
        roleFeatureService.updateRoleFeatures(roleId, featureIds);
        return ResponseEntity.ok().build();
    }
}
