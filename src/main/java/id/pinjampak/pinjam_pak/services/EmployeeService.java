package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.BranchResponseDTO;
import id.pinjampak.pinjam_pak.dto.RegisterEmployeeRequestDTO;
import id.pinjampak.pinjam_pak.dto.EmployeeResponseDTO;
import id.pinjampak.pinjam_pak.dto.UpdateEmployeeRequestDTO;
import id.pinjampak.pinjam_pak.models.*;
import id.pinjampak.pinjam_pak.repositories.*;
import id.pinjampak.pinjam_pak.security.CustomUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(UserRepository userRepository,
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

    public String registerEmployee(RegisterEmployeeRequestDTO requestDTO, CustomUserDetails currentUser) {
        if (!"SUPERADMIN".equalsIgnoreCase(currentUser.getUser().getRole().getNamaRole())) {
            return "❌ Hanya SUPERADMIN yang dapat mendaftarkan employee.";
        }

        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            return "❌ Username sudah digunakan.";
        }

        Optional<Role> roleOpt = roleRepository.findById(requestDTO.getRoleId());
        Optional<Branch> branchOpt = branchRepository.findById(requestDTO.getBranchId());

        if (roleOpt.isEmpty()) return "❌ Role tidak ditemukan.";
        if (branchOpt.isEmpty()) return "❌ Branch tidak ditemukan.";

        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setEmail(requestDTO.getEmail());
        user.setNama_lengkap(requestDTO.getNamaLengkap());
        user.setRole(roleOpt.get());
        userRepository.save(user);

        Employee employee = new Employee();
        employee.setNip(requestDTO.getNip());
        employee.setBranch(branchOpt.get());
        employee.setUser(user);

        employeeRepository.save(employee);

        return "✅ Employee berhasil didaftarkan.";
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(emp -> new EmployeeResponseDTO(
                emp.getEmployee_id(),
                emp.getNip(),
                emp.getBranch().getNamaCabang(),
                emp.getUser().getUsername(),
                emp.getUser().getEmail(),
                emp.getUser().getNama_lengkap(),
                emp.getUser().getRole().getNamaRole()
        )).collect(Collectors.toList());
    }

    public EmployeeResponseDTO getEmployeeById(UUID id) {
        return employeeRepository.findById(id).map(emp -> new EmployeeResponseDTO(
                emp.getEmployee_id(),
                emp.getNip(),
                emp.getBranch().getNamaCabang(),
                emp.getUser().getUsername(),
                emp.getUser().getEmail(),
                emp.getUser().getNama_lengkap(),
                emp.getUser().getRole().getNamaRole()
        )).orElse(null);
    }

    public String deleteEmployee(UUID id) {
        Optional<Employee> empOpt = employeeRepository.findById(id);
        if (empOpt.isPresent()) {
            employeeRepository.deleteById(id);
            userRepository.deleteById(empOpt.get().getUser().getUserId());
            return "✅ Employee berhasil dihapus.";
        }
        return "❌ Employee tidak ditemukan.";
    }

    public String updateEmployee(UUID id, UpdateEmployeeRequestDTO requestDTO) {
        Optional<Employee> empOpt = employeeRepository.findById(id);
        if (empOpt.isEmpty()) return "❌ Employee tidak ditemukan.";

        Employee employee = empOpt.get();
        User user = employee.getUser();

        Optional<Role> roleOpt = roleRepository.findByNamaRole(requestDTO.getRoleName());
        Optional<Branch> branchOpt = branchRepository.findByNamaCabang(requestDTO.getBranchName());
        if (roleOpt.isEmpty()) return "❌ Role tidak ditemukan.";
        if (branchOpt.isEmpty()) return "❌ Branch tidak ditemukan.";

        user.setRole(roleOpt.get());
        userRepository.save(user);

        employee.setBranch(branchOpt.get());
        employeeRepository.save(employee);

        return "✅ Employee berhasil diupdate.";
    }

    public Branch getBranchForCurrentUser(CustomUserDetails currentUser) {
        UUID userId = currentUser.getUser().getUserId();
        Optional<Employee> employeeOpt = employeeRepository.findByUser_UserId(userId);
        return employeeOpt.map(Employee::getBranch).orElse(null);
    }

    public BranchResponseDTO convertToDTO(Branch branch) {
        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setBranchId(branch.getBranchId().toString());
        dto.setNamaCabang(branch.getNamaCabang());
        dto.setAlamat(branch.getAlamat());
        return dto;
    }

    public List<BranchResponseDTO> getAllBranchDTOs() {
        List<Branch> branches = branchRepository.findAll();
        return branches.stream()
                .map(this::convertToDTO)
                .toList();
    }
}