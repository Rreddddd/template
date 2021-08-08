package security.listener;

import entity.Module;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AuthorizedEvent;
import org.springframework.security.web.FilterInvocation;
import pojo.Constant;
import util.Modules;

import javax.servlet.http.HttpServletRequest;

public class PublishAuthorizationSuccessListener implements ApplicationListener<AuthorizedEvent> {

    private static final String HOME_URL = "/home";

    @Override
    public void onApplicationEvent(AuthorizedEvent applicationEvent) {
        HttpServletRequest request = ((FilterInvocation) applicationEvent.getSource()).getRequest();
        String requestUrl = request.getServletPath();
        if (HOME_URL.equals(requestUrl)) {
            request.getSession().removeAttribute(Constant.CONTEXT_ACTIVE_MENU);
            return;
        }
        Module module = Modules.get(requestUrl);
        if (module != null) {
            request.getSession().setAttribute(Constant.CONTEXT_ACTIVE_MENU, module);
        }
    }
}