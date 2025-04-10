package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.CustomerRequestDTO;
import id.pinjampak.pinjam_pak.enums.ProvinceArea;
import id.pinjampak.pinjam_pak.helper.ProvinceAreaMapper;
import id.pinjampak.pinjam_pak.models.*;
import id.pinjampak.pinjam_pak.repositories.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public CustomerService(UserRepository userRepository, CustomerRepository customerRepository, BranchRepository branchRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.branchRepository = branchRepository;
    }

    @Transactional
    public void registerCustomer(String username, CustomerRequestDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        if (customerRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("User sudah terdaftar sebagai customer");
        }

        // Mapping area dari provinsi
        ProvinceArea area = ProvinceAreaMapper.getAreaByProvince(dto.getProvinsi());

        // Ambil 1 cabang dari area tersebut (nanti bisa diatur logikanya lebih dinamis)
        Branch branch = branchRepository.findFirstByArea(area)
                .orElseThrow(() -> new RuntimeException("Cabang tidak ditemukan untuk area " + area));

        double plafond = dto.getGaji() * 0.3;

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

        customerRepository.save(customer);
    }
}