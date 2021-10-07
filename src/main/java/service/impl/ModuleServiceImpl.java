package service.impl;

import dao.ModuleDao;
import entity.Menu;
import entity.Module;
import org.springframework.stereotype.Service;
import service.ModuleService;
import util.Modules;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleDao moduleDao;
    @Resource
    private MenuServiceImpl menuService;

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public List<Module> findPage(int pageIndex, int pageSize) {
        return moduleDao.findPage(pageIndex, pageSize);
    }

    @Override
    public Integer findCount() {
        return moduleDao.findCount();
    }

    @Override
    public void add(Module module) {
        moduleDao.add(module);
        Modules.put(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
        Modules.update(module);
    }

    @Override
    public void delete(int id) {
        moduleDao.delete(id);
        Modules.remove(id);
    }

    @PostConstruct
    private void initCache() {
        Modules.init(findAll());
        menuService.initCache();
    }
}
