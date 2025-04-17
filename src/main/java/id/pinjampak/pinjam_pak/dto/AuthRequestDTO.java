package id.pinjampak.pinjam_pak.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {
    private String usernameOrEmail;
    private String password;
}
