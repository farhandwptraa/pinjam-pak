package id.pinjampak.pinjam_pak.dto;
import lombok.Data;

import java.util.UUID;

@Data
public class RegisterEmployeeRequestDTO {
    private String username;
    private String password;
    private String email;
    private String namaLengkap;
    private Long nip;
    private UUID branchId; // id cabang
    private UUID roleId;   // id role (misal marketing, branch manager)
}