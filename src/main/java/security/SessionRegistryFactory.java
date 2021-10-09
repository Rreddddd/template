package security;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

/**
 * 因为不知道怎么xml配置的bean通过注解注入后不是一个实例，所以出此下策
 */
public abstract class SessionRegistryFactory {

    private static final SessionRegistry sessionRegistry = new SessionRegistryImpl();

    public static SessionRegistry getSingletonInstance() {
        return sessionRegistry;
    }
}
