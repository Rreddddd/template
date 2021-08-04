package controller;

import entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pojo.MsgResult;
import service.UserService;
import util.Context;
import util.WebPathUtil;

import javax.annotation.Resource;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private UserService userService;

    @RequestMapping("")
    public ModelAndView home() {
        return new ModelAndView("/jsp/home");
    }

    @PostMapping("/updateHeadImg")
    public MsgResult home(@RequestParam("uri") String uri) {
        User user = Context.getUser();
        if (user == null) {
            return MsgResult.failure("请刷新页面，再上传");
        }
        user = userService.findById(user.getId());
        String absoluteTempPath = WebPathUtil.ensureAbsolutePath(WebPathUtil.convertToUrl(uri));
        user.setHeadImg(WebPathUtil.getRelativePath(WebPathUtil.copyTempToFile(absoluteTempPath)));
        userService.add(user);
        return MsgResult.success();
    }
}