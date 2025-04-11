package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Branch;
import id.pinjampak.pinjam_pak.models.Employee;
import id.pinjampak.pinjam_pak.models.Pengajuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PengajuanRepository extends JpaRepository<Pengajuan, UUID> {
    List<Pengajuan> findByBranchManagerAndStatus(Employee manager, String status);

    // Hitung jumlah pengajuan aktif yang ditangani oleh marketing tertentu
    @Query("SELECT COUNT(p) FROM Pengajuan p WHERE p.marketing = :marketing AND p.status IN ('PENDING', 'REVIEWED')")
    int countActiveByMarketing(@Param("marketing") Employee marketing);

    @Query("SELECT e FROM Employee e WHERE e.branch = :branch AND e.user.role.namaRole = :roleName")
    List<Employee> findByBranchAndUserRoleNamaRole(@Param("branch") Branch branch, @Param("roleName") String roleName);

}

