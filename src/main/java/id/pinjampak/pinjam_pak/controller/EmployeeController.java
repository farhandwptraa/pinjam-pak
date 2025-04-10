package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.RegisterEmployeeRequestDTO;
import id.pinjampak.pinjam_pak.models.*;
import id.pinjampak.pinjam_pak.repositories.*;
import id.pinjampak.pinjam_pak.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(UserRepository userRepository,
                              RoleRepository roleRepository,
                              BranchRepository branchRepository,
                              EmployeeRepository employeeRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.branchRepository = branchRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody RegisterEmployeeRequestDTO requestDTO) {

        // ✅ Ambil user yang sedang login dari token
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // ✅ Cek apakah role-nya SUPERADMIN
        if (!"SUPERADMIN".equalsIgnoreCase(currentUser.getUser().getRole().getNamaRole())) {
            return ResponseEntity.status(403).body("❌ Hanya SUPERADMIN yang dapat mendaftarkan employee.");
        }

        // Cek username/email sudah dipakai
        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("❌ Username sudah digunakan.");
        }

        // Cek validitas role dan branch
        Optional<Role> roleOpt = roleRepository.findById(requestDTO.getRoleId());
        Optional<Branch> branchOpt = branchRepository.findById(requestDTO.getBranchId());

        if (roleOpt.isEmpty()) return ResponseEntity.badRequest().body("❌ Role tidak ditemukan.");
        if (branchOpt.isEmpty()) return ResponseEntity.badRequest().body("❌ Branch tidak ditemukan.");

        // Buat User
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setEmail(requestDTO.getEmail());
        user.setNama_lengkap(requestDTO.getNamaLengkap());
        user.setRole(roleOpt.get());
        userRepository.save(user);

        // Buat Employee
        Employee employee = new Employee();
        employee.setNip(requestDTO.getNip());
        employee.setBranch(branchOpt.get());
        employee.setUser(user);

        employeeRepository.save(employee);

        return ResponseEntity.ok("✅ Employee berhasil didaftarkan.");
    }
}