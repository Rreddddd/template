package controller;

import bean.HomeUserBean;
import entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pojo.MsgResult;
import security.MyDelegatingPasswordEncoder;
import security.UserPasswordEncoder;
import service.UserService;
import util.Context;
import util.StringUtils;
import util.WebPathUtil;

import javax.annotation.Resource;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private UserService userService;
    @Resource
    private MyDelegatingPasswordEncoder passwordEncoder;

    @RequestMapping("")
    public ModelAndView home() {
        return new ModelAndView("/jsp/home");
    }

    @PostMapping("/updateHeadImg")
    public MsgResult updateHeadImg(@RequestParam("uri") String uri) {
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

    @PostMapping("/updateUserInfo")
    public MsgResult updateUserInfo(@RequestBody HomeUserBean userBean) {
        User currentUser = Context.getUser();
        if (currentUser == null || !currentUser.getId().equals(userBean.getId())) {
            return MsgResult.failure("请刷新页面，再修改");
        }
        currentUser = userService.findById(userBean.getId());
        currentUser.setName(userBean.getName());
        currentUser.setPhone(userBean.getPhone());
        currentUser.setEmail(userBean.getEmail());
        if (StringUtils.isNotBlank(userBean.getNewPwd())) {
            if (!passwordEncoder.matches(userBean.getOldPwd(), currentUser.getPassword())) {
                return MsgResult.failure("旧密码错误");
            }
            currentUser.setPassword(passwordEncoder.encode(userBean.getNewPwd()));
        }
        userService.add(currentUser);
        return MsgResult.success();
    }
}