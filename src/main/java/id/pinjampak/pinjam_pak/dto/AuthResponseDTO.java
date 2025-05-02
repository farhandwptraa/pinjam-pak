package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String role_id; // tambahkan ini
    private String username;
    private String role;
}