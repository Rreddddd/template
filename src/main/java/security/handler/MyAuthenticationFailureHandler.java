package security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import pojo.MsgResult;
import service.UserService;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        int status = userService.verifyAccountAndPassword(httpServletRequest.getParameter("username"), httpServletRequest.getParameter("password"));
        MsgResult msgResult = MsgResult.failure();
        if (status == 0) {
            msgResult.setMsg("用户不存在");
        } else if (status == 1) {
            msgResult.setMsg("用户名或者密码错误");
        } else {
            msgResult.setMsg("系统错误");
        }
        httpServletResponse.getOutputStream().write(new ObjectMapper().writeValueAsBytes(msgResult));
    }
}
