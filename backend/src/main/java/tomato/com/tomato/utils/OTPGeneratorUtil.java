package tomato.com.tomato.utils;

import java.security.SecureRandom;

public class OTPGeneratorUtil {
    private static final String DIGITS = "0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();
    public static String optGenerator(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(DIGITS.length());
            otp.append(DIGITS.charAt(index));
        }
        return otp.toString();
    }
    // secure random is thread-safe so it can be initialized as static final.
    // the secureRando,.nextInt() will pick the a number from length and we store it as index.
    // Otp gets appended with char at index in DIGITS.
    // Length is otp length required
}
