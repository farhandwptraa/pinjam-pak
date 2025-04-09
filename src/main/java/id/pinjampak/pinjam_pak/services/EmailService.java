package id.pinjampak.pinjam_pak.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset Password Pinjam_Pak");
        message.setText("Klik link berikut untuk mereset password Anda:\n\n" + resetLink +
                "\n\nLink ini hanya berlaku selama 30 menit.");

        mailSender.send(message);
    }
}