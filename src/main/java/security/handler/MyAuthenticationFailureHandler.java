package security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import pojo.Constant;
import pojo.MsgResult;
import service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        MsgResult msgResult = MsgResult.failure();
        Object verifyState = httpServletRequest.getAttribute(Constant.LOGIN_VERIFY_STATUS);
        if (verifyState != null) {
            int _verifyState = (int) verifyState;
            if (_verifyState != Constant.VERIFY_STATUS_SUCCEED) {
                if (_verifyState == Constant.VERIFY_STATUS_FAIL) {
                    msgResult.setErrorCode(2);
                    msgResult.setMsg("验证码错误");
                } else if (_verifyState == Constant.VERIFY_STATUS_EXPIRED) {
                    msgResult.setErrorCode(4);
                    msgResult.setMsg("验证码过期");
                }
                httpServletResponse.getOutputStream().write(new ObjectMapper().writeValueAsBytes(msgResult));
                return;
            }
        }
        int status = userService.verifyAccountAndPassword(httpServletRequest.getParameter("username"), httpServletRequest.getParameter("password"));
        if (status == 0) {
            msgResult.setMsg("用户不存在");
        } else if (status == 1) {
            msgResult.setMsg("用户名或者密码错误");
        } else if (status == 3) {
            msgResult.setMsg("用户已被冻结");
        } else {
            msgResult.setMsg("系统错误");
        }
        httpServletResponse.getOutputStream().write(new ObjectMapper().writeValueAsBytes(msgResult));
    }
}
