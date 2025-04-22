package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.EmployeeResponseDTO;
import id.pinjampak.pinjam_pak.dto.RegisterEmployeeRequestDTO;
import id.pinjampak.pinjam_pak.dto.UpdateEmployeeRequestDTO;
import id.pinjampak.pinjam_pak.models.Branch;
import id.pinjampak.pinjam_pak.models.Role;
import id.pinjampak.pinjam_pak.security.CustomUserDetails;
import id.pinjampak.pinjam_pak.services.EmployeeService;
import id.pinjampak.pinjam_pak.repositories.BranchRepository;
import id.pinjampak.pinjam_pak.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody RegisterEmployeeRequestDTO requestDTO) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String response = employeeService.registerEmployee(requestDTO, currentUser);
        return response.startsWith("✅") ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        EmployeeResponseDTO result = employeeService.getEmployeeById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UpdateEmployeeRequestDTO dto) {
        String response = employeeService.updateEmployee(id, dto);
        return response.startsWith("✅") ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String response = employeeService.deleteEmployee(id);
        return response.startsWith("✅") ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    // 📌 Tambahan untuk ambil list cabang
    @GetMapping("/branches")
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchRepository.findAll());
    }

    // 📌 Tambahan untuk ambil list role
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }
}