package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByUser_UserId(UUID userId);
}