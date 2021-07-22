package security;

import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

public class UserPasswordEncoder extends DelegatingPasswordEncoder {
    /**
     * Creates a new instance
     *
     * @param idForEncode         the id used to lookup which {@link PasswordEncoder} should be
     *                            used for {@link #encode(CharSequence)}
     * @param idToPasswordEncoder a Map of id to {@link PasswordEncoder} used to determine
     *                            which {@link PasswordEncoder} should be used for {@link #matches(CharSequence, String)}
     */
    public UserPasswordEncoder(String idForEncode, Map<String, PasswordEncoder> idToPasswordEncoder) {
        super(idForEncode, idToPasswordEncoder);
    }
}
