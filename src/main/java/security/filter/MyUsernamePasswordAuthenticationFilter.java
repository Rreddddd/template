package security.filter;

import exception.CaptchaException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pojo.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Object loginCaptchaCode = request.getSession().getAttribute(Constant.LOGIN_CAPTCHA_CODE);
        if (loginCaptchaCode != null) {
            String _loginCaptchaCode = loginCaptchaCode.toString();
            String[] split = _loginCaptchaCode.split("-");
            _loginCaptchaCode = split[0];
            long validMilli = Long.parseLong(split[1]);
            if (System.currentTimeMillis() <= validMilli) {
                String verifyCode = request.getParameter("verifyCode");
                if (!_loginCaptchaCode.equalsIgnoreCase(verifyCode)) {
                    request.setAttribute(Constant.LOGIN_VERIFY_STATUS, Constant.VERIFY_STATUS_FAIL);
                    throw new CaptchaException("captcha code not matched!");
                }
            } else {
                request.setAttribute(Constant.LOGIN_VERIFY_STATUS, Constant.VERIFY_STATUS_EXPIRED);
                throw new CaptchaException("captcha code expired!");
            }
        } else {
            request.setAttribute(Constant.LOGIN_VERIFY_STATUS, Constant.VERIFY_STATUS_EXPIRED);
            throw new CaptchaException("captcha code expired!");
        }
        request.setAttribute(Constant.LOGIN_VERIFY_STATUS, Constant.VERIFY_STATUS_SUCCEED);
        return super.attemptAuthentication(request, response);
    }
}
