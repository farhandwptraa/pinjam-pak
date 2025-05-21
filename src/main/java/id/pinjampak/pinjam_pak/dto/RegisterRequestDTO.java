package id.pinjampak.pinjam_pak.dto;

import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor

public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private String namaLengkap;
}
