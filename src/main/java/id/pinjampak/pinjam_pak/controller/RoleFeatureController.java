package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.RoleFeatureDTO;
import id.pinjampak.pinjam_pak.services.RoleFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/role-feature")
@RequiredArgsConstructor
public class RoleFeatureController {

    private final RoleFeatureService roleFeatureService;

    @GetMapping("/all")
    public Map<String, Object> getAllRoleFeatures() {
        return roleFeatureService.getAllRoleFeatures();
    }

    @PostMapping("/update")
    public void updateRoleFeatures(@RequestBody List<RoleFeatureDTO> dtos) {
        roleFeatureService.updateRoleFeatures(dtos);
    }

    @GetMapping("/{roleId}")
    public List<UUID> getFeatureIdsByRole(@PathVariable UUID roleId) {
        return roleFeatureService.getFeatureIdsByRole(roleId);
    }
}