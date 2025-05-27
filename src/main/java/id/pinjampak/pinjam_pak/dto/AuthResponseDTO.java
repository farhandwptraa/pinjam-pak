package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String role_id;
    private String username;
    private String role;
    private String customerId;
    private String employeeId;
    private Boolean emailVerified;
}
