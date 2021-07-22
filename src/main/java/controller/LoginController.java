package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import util.Context;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView login() {
        if (Context.loginStatus()) {
            return new ModelAndView("redirect:/home");
        } else {
            return new ModelAndView("/pc/jsp/login");
        }
    }

    @RequestMapping("/logout")
    public void logout() {

    }
}
