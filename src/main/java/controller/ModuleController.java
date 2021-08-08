package controller;

import entity.Module;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
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
    public TableData<Module> getData() {
        TableData<Module> tableData = new TableData<>();
        List<Module> modules = moduleService.findAll();
        tableData.setRows(modules);
        tableData.setRecords(10);
        return tableData;
    }
}
