package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import util.Menus;

import java.util.Set;

@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("/jsp/menu_index");
    }

    @GetMapping("/data")
    public Set<Menus.InnerMenu> getData() {
        return Menus.getInnerTree().getChildren();
    }
}
