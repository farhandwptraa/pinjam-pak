package id.pinjampak.pinjam_pak.services;

import com.google.firebase.messaging.*;
import id.pinjampak.pinjam_pak.models.FcmToken;
import id.pinjampak.pinjam_pak.models.User;
import id.pinjampak.pinjam_pak.repositories.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifikasiService {

    private final FcmTokenRepository fcmTokenRepository;

    public void buatNotifikasi(User user, String pesan) {
        List<FcmToken> tokens = fcmTokenRepository.findByUser(user);

        for (FcmToken token : tokens) {
            sendPushNotification(token.getToken(), pesan);
        }
    }

    private void sendPushNotification(String fcmToken, String body) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .putData("title", "PinjamPak") // Untuk handler manual saat foreground
                .putData("body", body)
                .setNotification(Notification.builder() // ⬅️ WAJIB agar muncul saat app ditutup
                        .setTitle("PinjamPak")
                        .setBody(body)
                        .build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setSound("default")
                                .setChannelId("pinjampak_channel_id")
                                .build())
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Berhasil kirim FCM: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Gagal kirim FCM: " + e.getMessage());
        }
    }
}
