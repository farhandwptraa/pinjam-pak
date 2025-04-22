package id.pinjampak.pinjam_pak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID user_id;
    private String username;
    private String email;
    private String nama_lengkap;
    private String role;
}