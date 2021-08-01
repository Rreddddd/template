package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/error")
public class ErrorPageController {

    @RequestMapping("/403")
    public ModelAndView error403() {
        return new ModelAndView("/jsp/403");
    }

    @RequestMapping("/404")
    public ModelAndView error404() {
        return new ModelAndView("/jsp/404");
    }
}
