package security;

import org.springframework.security.crypto.password.PasswordEncoder;
import util.MD5Util;

public class UserMD5PasswordEncoder implements PasswordEncoder {

    private String prefixSaltStr;
    private String suffixSaltStr;

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.encrypt(addSalt(rawPassword).toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    private CharSequence addSalt(CharSequence rawPassword) {
        if (prefixSaltStr != null) {
            rawPassword = prefixSaltStr + rawPassword;
        }
        if (suffixSaltStr != null) {
            rawPassword += suffixSaltStr;
        }
        return rawPassword;
    }

    public void setPrefixSaltStr(String prefixSaltStr) {
        this.prefixSaltStr = prefixSaltStr;
    }

    public void setSuffixSaltStr(String suffixSaltStr) {
        this.suffixSaltStr = suffixSaltStr;
    }
}
