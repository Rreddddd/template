package service.impl;

import dao.MenuDao;
import entity.Menu;
import org.springframework.stereotype.Service;
import service.MenuService;
import util.Menus;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDao menuDao;

    @Override
    public List<Menu> findByParentId(int parentId) {
        return menuDao.findByParentId(parentId);
    }

    /**
     * 启动放入内存
     */
    @PostConstruct
    private void initCache() {
        Menus.init(null);
        initCache(findByParentId(-1));
    }

    private void initCache(List<Menu> menus) {
        if (menus != null && !menus.isEmpty()) {
            Menus.putAll(menus);
            for (Menu menu : menus) {
                initCache(findByParentId(menu.getId()));
            }
        }
    }
}
