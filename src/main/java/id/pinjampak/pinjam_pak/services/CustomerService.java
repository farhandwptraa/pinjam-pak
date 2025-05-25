package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.CustomerRequestDTO;
import id.pinjampak.pinjam_pak.dto.CustomerResponseDTO;
import id.pinjampak.pinjam_pak.enums.LoanLevel;
import id.pinjampak.pinjam_pak.models.*;
import id.pinjampak.pinjam_pak.repositories.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Value("${upload.ktp.dir}")
    private String uploadKtpDir;

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;
    private final ProvinceRepository provinceRepository;

    @Autowired
    public CustomerService(UserRepository userRepository, CustomerRepository customerRepository, BranchRepository branchRepository, ProvinceRepository provinceRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.branchRepository = branchRepository;
        this.provinceRepository = provinceRepository;
    }

    @Transactional
    public void registerCustomer(String username, CustomerRequestDTO dto, MultipartFile fotoKtp) {
        System.out.println("📥 Memulai proses pendaftaran customer...");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        if (customerRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("User sudah terdaftar sebagai customer");
        }

        Province province = provinceRepository.findByName(dto.getProvinsi())
                .orElseThrow(() -> new RuntimeException("Provinsi tidak ditemukan"));

        Branch branch = branchRepository.findFirstByArea(province.getArea())
                .orElseThrow(() -> new RuntimeException("Cabang tidak ditemukan untuk area " + province.getArea()));


        // ✅ Gunakan LoanLevel default
        LoanLevel defaultLoanLevel = LoanLevel.LEVEL_1;

        // ✅ Hitung plafon berdasarkan multiplier level 1
        double plafond = dto.getGaji() * defaultLoanLevel.getPlafondMultiplier();

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setNik(dto.getNik());
        customer.setTempat_lahir(dto.getTempat_lahir());
        customer.setTanggal_lahir(dto.getTanggal_lahir());
        customer.setPekerjaan(dto.getPekerjaan());
        customer.setGaji(dto.getGaji());
        customer.setPlafond(plafond);
        customer.setSisa_plafond(plafond);
        customer.setNo_hp(dto.getNo_hp());
        customer.setNama_ibu_kandung(dto.getNama_ibu_kandung());
        customer.setAlamat(dto.getAlamat());
        customer.setProvinsi(dto.getProvinsi());
        customer.setBranch(branch);
        customer.setLoanLevel(defaultLoanLevel); // ✅ Set default LoanLevel

        // 💾 Simpan file KTP
        String filename = UUID.randomUUID() + "_" + fotoKtp.getOriginalFilename();
        String fullPath = uploadKtpDir + filename;
        File dest = new File(fullPath);

        System.out.println("💾 Menyimpan file ke: " + fullPath);

        try {
            dest.getParentFile().mkdirs();
            fotoKtp.transferTo(dest);
            System.out.println("✅ File berhasil disimpan!");
            customer.setFotoKtpUrl("/uploads/ktp/" + filename);
        } catch (IOException | IllegalStateException e) {
            System.out.println("❌ Gagal menyimpan file KTP: " + e.getMessage());
            throw new RuntimeException("Gagal menyimpan file KTP", e);
        }

        customerRepository.save(customer);
        System.out.println("✅ Customer berhasil disimpan ke database.");
    }

    public Optional<CustomerResponseDTO> getCustomerDTOByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(customerRepository::findByUser)
                .map(customer -> {
                    CustomerResponseDTO dto = new CustomerResponseDTO();
                    dto.setUsername(customer.getUser().getUsername());
                    dto.setEmail(customer.getUser().getEmail());
                    dto.setNama_lengkap(customer.getUser().getNama_lengkap());

                    dto.setNik(customer.getNik());
                    dto.setTempat_lahir(customer.getTempat_lahir());
                    dto.setTanggal_lahir(customer.getTanggal_lahir());
                    dto.setPekerjaan(customer.getPekerjaan());
                    dto.setGaji(customer.getGaji());
                    dto.setPlafond(customer.getPlafond());
                    dto.setSisa_plafond(customer.getSisa_plafond());
                    dto.setNo_hp(customer.getNo_hp());
                    dto.setNama_ibu_kandung(customer.getNama_ibu_kandung());
                    dto.setAlamat(customer.getAlamat());
                    dto.setProvinsi(customer.getProvinsi());
                    dto.setNamaCabang(customer.getBranch().getNamaCabang());
                    dto.setAreaCabang(customer.getBranch().getArea().name());
                    dto.setLoanLevel(customer.getLoanLevel().name());

                    return dto;
                });
    }
}