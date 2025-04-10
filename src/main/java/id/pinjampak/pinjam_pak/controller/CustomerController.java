package id.pinjampak.pinjam_pak.controller;

import id.pinjampak.pinjam_pak.dto.CustomerRequestDTO;
import id.pinjampak.pinjam_pak.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(
            @RequestBody CustomerRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();

        customerService.registerCustomer(username, dto);

        return ResponseEntity.ok("Customer berhasil didaftarkan");
    }
}