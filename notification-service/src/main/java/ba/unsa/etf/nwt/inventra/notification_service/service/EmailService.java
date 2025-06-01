package ba.unsa.etf.nwt.inventra.notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmailToUsers(List<String> emails, String subject, String body) {
        for (String email : emails) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                log.info("Sent email to {}", email);
            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", email, e.getMessage());
            }
        }
    }
}
