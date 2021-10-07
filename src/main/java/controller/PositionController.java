package controller;

import entity.Position;
import entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pojo.MsgResult;
import pojo.TableData;
import service.PositionService;
import util.Users;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sys/position")
public class PositionController {

    @Resource
    private PositionService positionService;

    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("/jsp/position");
    }

    @GetMapping("/data")
    public TableData<Position> getData() {
        TableData<Position> tableData = new TableData<>();
        tableData.setRows(positionService.getWithUserAll());
        return tableData;
    }

    @GetMapping("/searchPersons")
    public List<User> searchPersons(@RequestParam(value = "searchKey", defaultValue = "") String searchKey) {
        return Users.search(searchKey);
    }

    @PostMapping("/add")
    public MsgResult add(@RequestBody Position position) {
        try {
            positionService.add(position);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }

    @PostMapping("/update")
    public MsgResult update(@RequestBody Position position) {
        try {
            positionService.update(position);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }

    @PostMapping("/delete")
    public MsgResult delete(@RequestParam(value = "id") int id) {
        try {
            positionService.delete(id);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }
}
