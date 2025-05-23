package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.dto.PengajuanResponseDTO;
import id.pinjampak.pinjam_pak.dto.PinjamanResponseDTO;
import id.pinjampak.pinjam_pak.models.Pengajuan;
import id.pinjampak.pinjam_pak.models.Pinjaman;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.PengajuanRepository;
import id.pinjampak.pinjam_pak.repositories.PinjamanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PengajuanRepository pengajuanRepository;
    private final PinjamanRepository pinjamanRepository;

    public List<PengajuanResponseDTO> getPengajuanByUser(User user) {
        return pengajuanRepository.findByUser(user).stream()
                .map(p -> new PengajuanResponseDTO(
                        p.getId_pengajuan(),
                        p.getAmount(),
                        p.getTenor(),
                        p.getStatus(),
                        p.getTanggalPengajuan(),
                        p.getLokasi()
                ))
                .toList();
    }

    public List<PinjamanResponseDTO> getPinjamanByUser(User user) {
        return pinjamanRepository.findByUser(user).stream()
                .map(p -> new PinjamanResponseDTO(
                        p.getId_pinjaman(),
                        p.getAmount(),
                        p.getStatus(),
                        p.getTanggalPencairan(),
                        p.getBunga()
                ))
                .toList();
    }
}

