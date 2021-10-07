package service;

import entity.Menu;
import entity.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface MenuService {

    List<Menu> findByParentId(int parentId);

    void save(List<Menu> menus, List<Permission> permissions);

    List<Map<String, Object>> getIconfont();
}
