package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.models.City;
import id.pinjampak.pinjam_pak.models.Province;
import id.pinjampak.pinjam_pak.services.WilayahService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/wilayah")
public class WilayahController {

    private final WilayahService wilayahService;

    @Autowired
    public WilayahController(WilayahService wilayahService) {
        this.wilayahService = wilayahService;
    }

    @GetMapping("/provinsi/{id}/kota")
    public ResponseEntity<List<String>> getCitiesByProvince(@PathVariable Long id) {
        List<City> cities = wilayahService.getCitiesByProvinceId(id);
        List<String> cityNames = cities.stream().map(City::getName).toList();
        return ResponseEntity.ok(cityNames);
    }

    @GetMapping("/provinsi")
    public ResponseEntity<List<Map<String, Object>>> getAllProvinces() {
        List<Province> provinces = wilayahService.getAllProvinces();
        List<Map<String, Object>> result = provinces.stream().map(prov -> {
            Map<String, Object> p = new HashMap<>();
            p.put("id", prov.getId());
            p.put("name", prov.getName());
            return p;
        }).toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/branch/{branchId}/provinsi")
    public ResponseEntity<Void> updateBranchProvinces(
            @PathVariable UUID branchId,
            @RequestBody List<Long> provinceIds
    ) {
        wilayahService.updateBranchProvinces(branchId, provinceIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/branch/{branchId}/provinsi")
    public ResponseEntity<List<Map<String, Object>>> getProvincesByBranch(
            @PathVariable UUID branchId
    ) {
        List<Province> provinces = wilayahService.getProvincesByBranchId(branchId);
        List<Map<String, Object>> result = provinces.stream().map(p -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", p.getId());
            m.put("name", p.getName());
            return m;
        }).toList();
        return ResponseEntity.ok(result);
    }
}

