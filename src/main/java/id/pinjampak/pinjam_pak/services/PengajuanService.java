package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.CreatePengajuanRequestDTO;
import id.pinjampak.pinjam_pak.dto.MarketingReviewRequestDTO;
import id.pinjampak.pinjam_pak.enums.LoanLevel;
import id.pinjampak.pinjam_pak.models.*;
import id.pinjampak.pinjam_pak.repositories.EmployeeRepository;
import id.pinjampak.pinjam_pak.repositories.PengajuanRepository;
import id.pinjampak.pinjam_pak.repositories.UserRepository;
import id.pinjampak.pinjam_pak.repositories.PinjamanRepository;
import id.pinjampak.pinjam_pak.repositories.CustomerRepository;
import id.pinjampak.pinjam_pak.repositories.RoleFeatureRepository;
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
    private final RoleFeatureRepository roleFeatureRepository;


    public void buatPengajuan(CreatePengajuanRequestDTO request, String username) {
        // Ambil user berdasarkan username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));

        Customer customer = user.getCustomer();
        if (customer == null) {
            throw new IllegalArgumentException("User bukan customer");
        }

        Branch customerBranch = customer.getBranch();

        // Ambil semua employee dari branch ini yang role-nya MARKETING
        List<Employee> marketings = employeeRepository.findAll().stream()
                .filter(emp -> emp.getBranch().equals(customerBranch)
                        && emp.getUser().getRole().getNamaRole().equalsIgnoreCase("MARKETING"))
                .toList();

        if (marketings.isEmpty()) {
            throw new IllegalStateException("Tidak ada marketing untuk cabang ini");
        }

        // Pilih marketing dengan jumlah pengajuan aktif paling sedikit
        Employee selectedMarketing = marketings.stream()
                .min(Comparator.comparingInt(pengajuanRepository::countActiveByMarketing))
                .orElseThrow();

        // Ambil level dan plafond maksimum
        LoanLevel level = customer.getLoanLevel();
        double gaji = customer.getGaji();
        double maxAllowed = gaji * level.getPlafondMultiplier();

        // Validasi tenor
        if (request.getTenor() > level.getMaxTenor()) {
            throw new IllegalArgumentException(String.format(
                    "Tenor melebihi batas untuk level %s: maksimum %d bulan",
                    level, level.getMaxTenor()));
        }

        // Hitung bunga dan amountFinal
        double bunga = level.getBungaByTenor(request.getTenor());
        double amount = request.getAmount();
        double amountFinal = amount + (amount * bunga * request.getTenor());

        // Validasi plafon berdasarkan amountFinal
        if (amount > maxAllowed) {
            throw new IllegalArgumentException(String.format(
                    "Jumlah pengajuan (termasuk bunga) melebihi batas plafon untuk level %s: maksimum %.2f",
                    level, maxAllowed));
        }

        // Buat dan simpan entitas pengajuan
        Pengajuan pengajuan = new Pengajuan();
        pengajuan.setUser(user);
        pengajuan.setMarketing(selectedMarketing);
        pengajuan.setAmount(request.getAmount());
        pengajuan.setTenor(request.getTenor());
        pengajuan.setBunga(bunga);
        pengajuan.setAmountFinal(amountFinal);
        pengajuan.setMaxPlafond(maxAllowed);
        pengajuan.setStatus("PENDING");
        pengajuan.setTanggalPengajuan(LocalDateTime.now());
        pengajuan.setLokasi(request.getLokasi());
        System.out.println("Lokasi pengajuan: " + request.getLokasi());

        pengajuanRepository.save(pengajuan);

        // Kirim notifikasi ke customer
        notifikasiService.buatNotifikasi(
                user,
                String.format("Pengajuan Anda disimpan: bunga %.2f%%/bulan, tenor %d bulan. Total yang harus dikembalikan: Rp %.2f",
                        bunga * 100, request.getTenor(), amountFinal));
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
        // UUID fitur 'pengajuan.disburse'
        UUID PENGAJUAN_DISBURSE_FEATURE_ID = UUID.fromString("8D9DE09C-AFB9-4988-ABDA-94C204347914");

        // Cari pengajuan
        Pengajuan pengajuan = pengajuanRepository.findById(idPengajuan)
                .orElseThrow(() -> new NoSuchElementException("Pengajuan tidak ditemukan"));

        if (!pengajuan.getStatus().equalsIgnoreCase("APPROVED")) {
            throw new IllegalStateException("Pengajuan belum disetujui oleh Manager");
        }

        // Cari user yang login
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User tidak ditemukan"));

        // Cek apakah role user punya akses ke fitur pengajuan.disburse
        Role role = user.getRole();
        boolean hasAccess = roleFeatureRepository.existsByRole_RoleIdAndFeature_FeatureId(role.getRoleId(), PENGAJUAN_DISBURSE_FEATURE_ID);
        if (!hasAccess) {
            throw new AccessDeniedException("Role Anda tidak memiliki akses ke fitur pencairan pengajuan");
        }

        // Validasi role harus BACKOFFICE
        if (!role.getNamaRole().equalsIgnoreCase("BACKOFFICE")) {
            throw new AccessDeniedException("Anda bukan Backoffice");
        }

        // Validasi cabang employee harus sama dengan cabang marketing pengajuan
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
        pinjaman.setAmount((int) pengajuan.getAmountFinal());
        pinjaman.setBunga(pengajuan.getBunga());   // ⬅️ pindahkan bunga
        pinjaman.setStatus("AKTIF");
        pinjaman.setTanggalPencairan(LocalDateTime.now());
        pinjamanRepository.save(pinjaman);

        // Update sisa plafond customer
        Customer customer = pengajuan.getUser().getCustomer();
        double sisaPlafond = customer.getSisa_plafond();
        double amount = pengajuan.getAmount();
        double amountFinal = pengajuan.getAmountFinal();

        if (amount > sisaPlafond) {
            throw new IllegalStateException("Sisa plafond tidak mencukupi");
        }

        customer.setSisa_plafond(sisaPlafond - amountFinal);
        customerRepository.save(customer);

        // Kirim notifikasi ke customer
        notifikasiService.buatNotifikasi(pengajuan.getUser(), "Pinjaman Anda telah dicairkan.");
    }
}