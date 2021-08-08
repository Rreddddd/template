package service.impl;

import dao.PermissionDao;
import org.springframework.stereotype.Service;
import service.PermissionService;
import util.Permissions;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    @PostConstruct
    private void initCache() {
        Permissions.init(permissionDao.findAll());
    }
}
