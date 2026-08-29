package tomato.com.tomato.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@Async
public class EmailDispacterUtil {

    @Value("${spring.mail.email}")
    private String mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    public void emailDispatch(String reciever, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(mailSender);
            mailMessage.setTo(reciever);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
           System.out.print("Error email was not send");
        }
    }
}
