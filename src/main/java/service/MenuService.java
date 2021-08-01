package service;

import entity.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> findByParentId(int parentId);
}
