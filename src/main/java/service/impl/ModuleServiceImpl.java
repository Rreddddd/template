package service.impl;

import dao.ModuleDao;
import entity.Module;
import org.springframework.stereotype.Service;
import service.ModuleService;
import util.Modules;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleDao moduleDao;

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @PostConstruct
    private void initCache() {
        Modules.init(findAll());
    }
}
