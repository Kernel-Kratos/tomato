package tomato.com.tomato.service.email;

public interface IEmailService {
 void emailSender(String receiverEmail);

 String emailVerifiedTokenGenerator(String reciverEmail);

 boolean emailOtpVerification(String recieverEmail, String otp);
 
 boolean emailTokenVerification(String submittedEmail, String submittedToken);
}
