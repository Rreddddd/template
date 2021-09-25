package controller;

import entity.Module;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pojo.MsgResult;
import pojo.TableData;
import service.ModuleService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sys/module")
public class ModuleController {

    @Resource
    private ModuleService moduleService;

    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("/jsp/module_index");
    }

    @GetMapping("/data")
    public TableData<Module> getData(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                     @RequestParam(value = "pageSize", defaultValue = "25") int pageSize) {
        TableData<Module> tableData = new TableData<>();
        List<Module> modules = moduleService.findPage(pageIndex, pageSize);
        tableData.setRows(modules);
        tableData.setRecords(moduleService.findCount());
        return tableData;
    }

    @PostMapping("/add")
    public MsgResult add(@RequestBody Module module) {
        try {
            moduleService.add(module);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }

    @PostMapping("/update")
    public MsgResult update(@RequestBody Module module) {
        try {
            moduleService.update(module);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }

    @PostMapping("/delete")
    public MsgResult delete(@RequestParam(value = "id") int id) {
        try {
            moduleService.delete(id);
            return MsgResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return MsgResult.failure();
        }
    }
}
