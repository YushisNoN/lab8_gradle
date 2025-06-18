package utils;
import java.security.SecureRandom;

public class CaptchaGenerator {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    // Генерирует капчу длиной length
    public static String generateCaptcha(int length) {
        StringBuilder captcha = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            captcha.append(CHAR_POOL.charAt(index));
        }
        return captcha.toString();
    }

}
