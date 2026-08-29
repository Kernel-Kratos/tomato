package tomato.com.tomato.service.email;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import tomato.com.tomato.utils.EmailDispacterUtil;
import tomato.com.tomato.utils.OTPGeneratorUtil;

@Service
public class EmailService implements IEmailService {
    private final StringRedisTemplate redisTemplate;
    private final EmailDispacterUtil emailDispacterUtil;
    public EmailService (StringRedisTemplate redisTemplate, EmailDispacterUtil emailDispacterUtil){
        this.redisTemplate = redisTemplate;
        this.emailDispacterUtil = emailDispacterUtil;
    }
    private static final String emailBody = "Your One-time password is: ";
    private static final String emailSubject = "is your OTP";
    @Override
    public void emailSender(String recieverEmail) {
        String otp = OTPGeneratorUtil.optGenerator(6); 
        String redisKey = "otp:" + recieverEmail; // "" is value and other is key
        redisTemplate.opsForValue().set(redisKey, otp, Duration.ofMinutes(10));
        emailDispacterUtil.emailDispatch(recieverEmail, otp + " " + emailSubject, emailBody + " " + otp);
        redisTemplate.expire(redisKey, Duration.ofMinutes(10));
    }

    @Override
    public boolean emailOtpVerification(String submittedEmail, String submittedOtp){
        String redisKey = "otp:" + submittedEmail;
        String redisOtp = redisTemplate.opsForValue().get(redisKey);
        if (redisOtp != null && redisOtp.equals(submittedOtp)){
            redisTemplate.delete(redisKey);
            return true;
        }
        return false;
    }

    @Override
    public String emailVerifiedTokenGenerator(String recieverEmail) {
        String token = OTPGeneratorUtil.optGenerator(10);
        String redisKey = "token:" + recieverEmail;
        redisTemplate.opsForValue().set(redisKey, token, Duration.ofMinutes(10));
        return token;
    }

    @Override
    public boolean emailTokenVerification(String submittedEmail, String submittedToken) {
        String redisKey = "token:" + submittedEmail;
        String token = redisTemplate.opsForValue().get(redisKey);
        if(token != null && token.equals(submittedToken)){
            redisTemplate.delete(redisKey);
            return true;
        }
        return false;
    }

}
