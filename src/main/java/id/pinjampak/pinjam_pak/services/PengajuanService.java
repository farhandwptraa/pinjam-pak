package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.CreatePengajuanRequestDTO;
import id.pinjampak.pinjam_pak.dto.MarketingReviewRequestDTO;
import id.pinjampak.pinjam_pak.dto.PengajuanListResponseDTO;
import id.pinjampak.pinjam_pak.models.*;
import id.pinjampak.pinjam_pak.repositories.EmployeeRepository;
import id.pinjampak.pinjam_pak.repositories.PengajuanRepository;
import id.pinjampak.pinjam_pak.repositories.UserRepository;
import id.pinjampak.pinjam_pak.repositories.PinjamanRepository;
import id.pinjampak.pinjam_pak.repositories.CustomerRepository;
import id.pinjampak.pinjam_pak.services.NotifikasiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PengajuanService {

    private final PengajuanRepository pengajuanRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PinjamanRepository pinjamanRepository;
    private final NotifikasiService notifikasiService;


    public void buatPengajuan(CreatePengajuanRequestDTO request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));

        if (user.getCustomer() == null) {
            throw new IllegalArgumentException("User bukan customer");
        }

        Double sisaPlafond = user.getCustomer().getSisa_plafond();
        if (request.getAmount() > sisaPlafond) {
            throw new IllegalArgumentException("Jumlah pengajuan melebihi sisa plafon");
        }

        Branch customerBranch = user.getCustomer().getBranch();

        // Ambil semua employee dari branch ini yg role-nya MARKETING
        List<Employee> marketings = employeeRepository.findAll().stream()
                .filter(emp ->
                        emp.getBranch().equals(customerBranch) &&
                                emp.getUser().getRole().getNamaRole().equalsIgnoreCase("MARKETING"))
                .toList();

        if (marketings.isEmpty()) {
            throw new IllegalStateException("Tidak ada marketing untuk cabang ini");
        }

        // Pilih marketing dengan pengajuan aktif paling sedikit
        Employee selectedMarketing = marketings.stream()
                .min(Comparator.comparingInt(pengajuanRepository::countActiveByMarketing))
                .orElseThrow();

        Pengajuan pengajuan = new Pengajuan();
        pengajuan.setUser(user);
        pengajuan.setMarketing(selectedMarketing);
        pengajuan.setAmount(request.getAmount());
        pengajuan.setStatus("PENDING");
        pengajuan.setTanggalPengajuan(LocalDateTime.now());
        pengajuan.setTenor(request.getTenor());

        pengajuanRepository.save(pengajuan);

        notifikasiService.buatNotifikasi(user, "Pengajuan pinjaman Anda telah dikirim.");
    }

    public void reviewOlehMarketing(UUID idPengajuan, MarketingReviewRequestDTO request, String username) {
        Pengajuan pengajuan = pengajuanRepository.findById(idPengajuan)
                .orElseThrow(() -> new NoSuchElementException("Pengajuan tidak ditemukan"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User tidak ditemukan"));

        Employee marketing = user.getEmployee();
        pengajuan.setMarketing(marketing); // <-- penting untuk set dulu sebelum akses branch-nya

        // Validasi user yang sedang login adalah marketing dari pengajuan ini
        if (!pengajuan.getMarketing().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Anda bukan marketing yang ditugaskan untuk pengajuan ini");
        }

        if (!pengajuan.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Pengajuan sudah diproses");
        }

        pengajuan.setCatatanMarketing(request.getCatatan());
        pengajuan.setTanggalDisetujuiMarketing(LocalDateTime.now());

        if (request.isDisetujui()) {
            pengajuan.setStatus("REVIEWED");

            Branch branch = pengajuan.getMarketing().getBranch();

            List<Employee> managers = employeeRepository.findAll().stream()
                    .filter(emp ->
                            emp.getBranch().equals(branch) &&
                                    emp.getUser().getRole().getNamaRole().equalsIgnoreCase("MANAGER"))
                    .toList();

            if (managers.isEmpty()) {
                throw new IllegalStateException("Tidak ada Branch Manager untuk cabang ini");
            }

            pengajuan.setBranchManager(managers.get(0)); // ambil salah satu
        } else {
            pengajuan.setStatus("REJECTED");
        }

        pengajuanRepository.save(pengajuan);
        notifikasiService.buatNotifikasi(pengajuan.getUser(), "Pengajuan Anda sedang direview oleh Marketing.");
    }

    public List<Pengajuan> getPengajuanPendingUntukMarketing(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));

        if (user.getEmployee() == null) {
            throw new IllegalArgumentException("User bukan employee");
        }

        Employee marketing = user.getEmployee();

        return pengajuanRepository.findAll().stream()
                .filter(p -> p.getStatus().equals("PENDING") &&
                        p.getMarketing().getEmployee_id().equals(marketing.getEmployee_id()))
                .toList();
    }

    public void reviewByBranchManager(UUID idPengajuan, String username, boolean disetujui, String catatan) {
        Pengajuan pengajuan = pengajuanRepository.findById(idPengajuan)
                .orElseThrow(() -> new NoSuchElementException("Pengajuan tidak ditemukan"));

        // Validasi user adalah Branch Manager yang ditugaskan
        if (!pengajuan.getBranchManager().getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Anda bukan Branch Manager untuk pengajuan ini");
        }

        if (!pengajuan.getStatus().equals("REVIEWED")) {
            throw new IllegalStateException("Pengajuan tidak dalam status REVIEWED");
        }

        pengajuan.setCatatanManager(catatan);
        pengajuan.setTanggalDisetujuiManager(LocalDateTime.now());

        if (disetujui) {
            pengajuan.setStatus("APPROVED");
            notifikasiService.buatNotifikasi(pengajuan.getUser(), "Pengajuan Anda telah disetujui oleh Branch Manager.");
        } else {
            pengajuan.setStatus("REJECTED");
        }

        pengajuanRepository.save(pengajuan);
    }

    public List<Pengajuan> getPengajuanPendingUntukManager(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User tidak ditemukan"));

        Employee manager = user.getEmployee();
        if (manager == null) {
            throw new IllegalStateException("User ini bukan employee / manager");
        }

        return pengajuanRepository.findByBranchManagerAndStatus(manager, "REVIEWED");
    }

    public void disbursePengajuan(UUID idPengajuan, String username) {
        Pengajuan pengajuan = pengajuanRepository.findById(idPengajuan)
                .orElseThrow(() -> new NoSuchElementException("Pengajuan tidak ditemukan"));

        if (!pengajuan.getStatus().equals("APPROVED")) {
            throw new IllegalStateException("Pengajuan belum disetujui oleh Manager");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User tidak ditemukan"));

        if (!user.getRole().getNamaRole().equalsIgnoreCase("BACKOFFICE")) {
            throw new AccessDeniedException("Anda bukan Backoffice");
        }

        Employee employee = user.getEmployee();
        if (employee == null || !employee.getBranch().equals(pengajuan.getMarketing().getBranch())) {
            throw new AccessDeniedException("Anda tidak memiliki akses ke pengajuan ini");
        }

        // Update status pengajuan
        pengajuan.setBackOffice(employee);
        pengajuan.setTanggalPencairan(LocalDateTime.now());
        pengajuan.setStatus("DISBURSED");
        pengajuanRepository.save(pengajuan);

        // Simpan ke tabel pinjaman
        Pinjaman pinjaman = new Pinjaman();
        pinjaman.setUser(pengajuan.getUser());
        pinjaman.setAmount(pengajuan.getAmount());
        pinjaman.setStatus("AKTIF");
        pinjaman.setTanggalPencairan(LocalDateTime.now());
        pinjamanRepository.save(pinjaman);

        // Update sisa plafond
        Customer customer = pengajuan.getUser().getCustomer();
        double sisaPlafond = customer.getSisa_plafond();
        double amount = pengajuan.getAmount();

        if (amount > sisaPlafond) {
            throw new IllegalStateException("Sisa plafond tidak mencukupi");
        }

        customer.setSisa_plafond(sisaPlafond - amount);
        customerRepository.save(customer);
        notifikasiService.buatNotifikasi(pengajuan.getUser(), "Pinjaman Anda telah dicairkan.");
    }

    public List<PengajuanListResponseDTO> getAllPengajuanByRole(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));

        if (user.getEmployee() == null) {
            throw new AccessDeniedException("Hanya employee yang dapat melihat pengajuan");
        }

        Employee employee = user.getEmployee();
        String roleName = employee.getUser().getRole().getNamaRole();

        List<Pengajuan> pengajuans;

        if (roleName.equalsIgnoreCase("MARKETING")) {
            pengajuans = pengajuanRepository.findAll().stream()
                    .filter(p ->
                            p.getMarketing() != null &&
                                    p.getMarketing().getEmployee_id().equals(employee.getEmployee_id()) &&
                                    !p.getStatus().equalsIgnoreCase("DISBURSED")
                    )
                    .toList();
        } else if (roleName.equalsIgnoreCase("MANAGER") || roleName.equalsIgnoreCase("BACKOFFICE")) {
            Branch branch = employee.getBranch();
            pengajuans = pengajuanRepository.findAll().stream()
                    .filter(p ->
                            p.getMarketing() != null &&
                                    p.getMarketing().getBranch().getBranch_id().equals(branch.getBranch_id()) &&
                                    !p.getStatus().equalsIgnoreCase("DISBURSED")
                    )
                    .toList();
        } else {
            throw new AccessDeniedException("Role tidak diizinkan melihat data pengajuan");
        }

        return pengajuans.stream()
                .map(p -> new PengajuanListResponseDTO(
                        p.getId_pengajuan(),
                        p.getUser().getCustomer().getUser().getNama_lengkap(),
                        p.getAmount(),
                        p.getStatus(),
                        p.getTanggalPengajuan(),
                        p.getMarketing() != null ? p.getMarketing().getUser().getUsername() : "-"
                ))
                .toList();
    }
}