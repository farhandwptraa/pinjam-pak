package id.pinjampak.pinjam_pak.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.pinjampak.pinjam_pak.dto.CustomerRequestDTO;
import id.pinjampak.pinjam_pak.dto.CustomerResponseDTO;
import id.pinjampak.pinjam_pak.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerCustomer(
            @RequestPart("data") String rawData,
            @RequestPart("fotoKtp") MultipartFile fotoKtp,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        System.out.println("ROOT DIRECTORY: " + System.getProperty("user.dir"));
        System.out.println("Raw JSON: " + rawData); // Log isi JSON
        ObjectMapper mapper = new ObjectMapper();
        CustomerRequestDTO dto = null;
        try {
            dto = mapper.readValue(rawData, CustomerRequestDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Gagal parsing JSON", e);
        }

        String username = userDetails.getUsername();
        customerService.registerCustomer(username, dto, fotoKtp);
        return ResponseEntity.ok("Customer berhasil didaftarkan");
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerResponseDTO> getCustomerData(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return customerService.getCustomerDTOByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}