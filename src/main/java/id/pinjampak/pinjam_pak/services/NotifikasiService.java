package id.pinjampak.pinjam_pak.services;

import id.pinjampak.pinjam_pak.models.Notifikasi;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.NotifikasiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotifikasiService {

    @Autowired
    private NotifikasiRepository notifikasiRepository;

    public void buatNotifikasi(User user, String isi) {
        Notifikasi notifikasi = new Notifikasi();
        notifikasi.setUser(user);
        notifikasi.setIsi(isi);
        notifikasi.setWaktuDibuat(LocalDateTime.now());
        notifikasi.setDibaca(false);
        notifikasiRepository.save(notifikasi);
    }

    public List<Notifikasi> getNotifikasiUntukUser(User user) {
        return notifikasiRepository.findByUserOrderByWaktuDibuatDesc(user);
    }
}