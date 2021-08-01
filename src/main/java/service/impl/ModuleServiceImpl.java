package service.impl;

import dao.ModuleDao;
import entity.Module;
import org.springframework.stereotype.Service;
import service.ModuleService;

import javax.annotation.Resource;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleDao moduleDao;

    @Override
    public Module findOne(int id) {
        return moduleDao.findOne(id);
    }
}
