package id.pinjampak.pinjam_pak.repositories;

import id.pinjampak.pinjam_pak.models.Customer;
import id.pinjampak.pinjam_pak.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByUser(User user);
}