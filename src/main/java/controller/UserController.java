package controller;

import entity.Position;
import entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pojo.MsgResult;
import pojo.TableData;
import security.MyDelegatingPasswordEncoder;
import service.UserService;
import util.Positions;
import util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private MyDelegatingPasswordEncoder passwordEncoder;

    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("/jsp/user");
    }

    @GetMapping("/data")
    public TableData<User> getData() {
        TableData<User> tableData = new TableData<>();
        tableData.setRows(userService.getWithPositionAll());
        return tableData;
    }

    @GetMapping("/searchPositions")
    public List<Position> searchPositions(@RequestParam(value = "searchKey", defaultValue = "") String searchKey) {
        return Positions.search(searchKey);
    }

    @PostMapping("/add")
    public MsgResult add(@RequestBody User user) {
        try {
            if (userService.findByAccount(user.getAccount()) != null) {
                return MsgResult.failure("用户名“" + user.getAccount() + "”已存在");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreateTime(new Date());
            userService.add(user);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }

    @PostMapping("/update")
    public MsgResult update(@RequestBody User user) {
        try {
            User oldUser = userService.findById(user.getId());
            if (StringUtils.isNotBlank(user.getPassword())) {
                oldUser.setPassword(passwordEncoder.encode(oldUser.getPassword()));
            }
            oldUser.setName(user.getName());
            oldUser.setPhone(user.getPhone());
            oldUser.setEmail(user.getEmail());
            oldUser.setPositions(user.getPositions());
            userService.update(oldUser);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }

    @PostMapping("/delete")
    public MsgResult delete(@RequestParam(value = "id") int id) {
        try {
            userService.delete(id);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }
}
