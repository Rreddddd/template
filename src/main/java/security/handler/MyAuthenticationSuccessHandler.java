package security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import pojo.MsgResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private SessionAuthenticationStrategy sessionStrategy;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        //记录session信息 经测试踢不掉人等找到解决方案后再放开
//        sessionStrategy.onAuthentication(authentication, httpServletRequest, httpServletResponse);
        String requestType = httpServletRequest.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(requestType)) {
            httpServletResponse.getOutputStream().write(new ObjectMapper().writeValueAsBytes(MsgResult.success()));
        } else {
            httpServletResponse.sendRedirect("/home");
        }
    }

    public void setSessionStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }
}
