package id.pinjampak.pinjam_pak.dto;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {
    private Long nip;
    private UUID branchId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String role;
}