package id.pinjampak.pinjam_pak.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequestDTO {
    private String branchName;
    private String roleName;
}