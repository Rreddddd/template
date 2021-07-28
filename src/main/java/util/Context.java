package util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class Context {

    private static SecurityContext getCurrentContext() {
        return SecurityContextHolder.getContext();
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getSession();
    }

    public static boolean loginStatus() {
        return !(getCurrentContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }
}
