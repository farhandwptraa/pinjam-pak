package id.pinjampak.pinjam_pak.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {
    private String oldPassword;
    private String newPassword;
}