package id.pinjampak.pinjam_pak.dto;

import lombok.*;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private UUID employeeId;
    private Long nip;
    private String namaCabang;
    private String username;
    private String email;
    private String nama_lengkap;
    private String role;
}