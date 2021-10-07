package controller;

import bean.MenuFormData;
import entity.Position;
import entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pojo.MsgResult;
import service.MenuService;
import service.ModuleService;
import util.Menus;
import util.Positions;
import util.Users;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    @Resource
    private ModuleService moduleService;
    @Resource
    private MenuService menuService;

    @GetMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/jsp/menu_index");
        modelAndView.getModelMap().addAttribute("modules", moduleService.findAll());
        return modelAndView;
    }

    @GetMapping("/data")
    public Set<Menus.InnerMenu> getData() {
        return Menus.getInnerTree().getChildren();
    }

    @RequestMapping("/searchAuths")
    public List<Map<String, Object>> searchAuths(@RequestParam(value = "searchKey", defaultValue = "") String searchKey) {
        List<Map<String, Object>> auths = new ArrayList<>();
        Map<String, Object> auth;
        List<User> users = Users.search(searchKey);
        for (User user : users) {
            auth = new HashMap<>();
            auth.put("id", user.getId() + "_0");
            auth.put("value", user.getName());
            auth.put("unit", "人员");
            auths.add(auth);
        }
        List<Position> positions = Positions.search(searchKey);
        for (Position position : positions) {
            auth = new HashMap<>();
            auth.put("id", position.getId() + "_1");
            auth.put("value", position.getName());
            auth.put("unit", "职位");
            auths.add(auth);
        }
        return auths;
    }

    @GetMapping("/getIconfont")
    public List<Map<String, Object>> getIconfont() {
        return menuService.getIconfont();
    }

    @PostMapping("/save")
    public MsgResult save(@RequestBody MenuFormData formData) {
        try {
            menuService.save(formData.getMenus(), formData.getPermissions());
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }
}
