package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("/pc/jsp/login");
    }

    @RequestMapping("/logout")
    public void logout() {

    }
}
