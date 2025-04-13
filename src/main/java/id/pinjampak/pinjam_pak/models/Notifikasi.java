package id.pinjampak.pinjam_pak.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifikasi")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notifikasi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String isi;

    @Column(name = "waktu_dibuat", nullable = false)
    private LocalDateTime waktuDibuat;

    @Column(name = "dibaca", nullable = false)
    private boolean dibaca;
}