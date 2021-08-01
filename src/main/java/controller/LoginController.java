package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pojo.Constant;
import util.CaptchaUtil;
import util.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView login() {
        if (Context.loginStatus()) {
            return new ModelAndView("redirect:/home");
        } else {
            return new ModelAndView("/jsp/login");
        }
    }

    @RequestMapping("/logout")
    public void logout() {

    }

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("cache-control", "no-cache");
        response.setHeader("Expire", "-1");
        response.setHeader("pragma", "no-cache");
        response.setContentType("image/jpeg");
        String code = CaptchaUtil.random(80, 30, response.getOutputStream());
        request.getSession().setAttribute(Constant.LOGIN_CAPTCHA_CODE, code + "-" + (System.currentTimeMillis() + 60000L));
    }
}
