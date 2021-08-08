package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class MD5Util {

    public static String encrypt(CharSequence str) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(str.toString().getBytes(StandardCharsets.UTF_8));
            byte[] bytes = m.digest();
            StringBuilder result = new StringBuilder();
            for (byte aByte : bytes) {
                result.append(Integer.toHexString((0x000000FF & aByte) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
