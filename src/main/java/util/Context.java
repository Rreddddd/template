package util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class Context {

    public static boolean loginStatus() {
        return !(getCurrentContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    private static SecurityContext getCurrentContext() {
        return SecurityContextHolder.getContext();
    }
}
