package util;

import entity.Module;
import entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pojo.Constant;

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

    public static boolean isPhone() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return false;
        }
        String[] deviceArray = new String[]{"android", "mac os", "windows phone"};
        String userAgent = request.getHeader("user-agent").toLowerCase();
        for (String s : deviceArray) {
            if (userAgent.indexOf(s) > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean loginStatus() {
        return !(getCurrentContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    public static User getUser() {
        Authentication authentication = getCurrentContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return Users.get(authentication.getName());
    }

    public static Module getActiveModule() {
        HttpSession session = getSession();
        if (session == null) {
            return null;
        }
        return (Module) session.getAttribute(Constant.CONTEXT_ACTIVE_MENU);
    }
}
