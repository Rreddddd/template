package service.impl;

import dao.PermissionDao;
import org.springframework.stereotype.Service;
import service.PermissionService;

import javax.annotation.Resource;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;
}
