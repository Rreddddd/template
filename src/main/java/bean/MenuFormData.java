package bean;

import entity.Menu;
import entity.Permission;

import java.util.List;

public class MenuFormData {

    private List<Menu> menus;
    private List<Permission> permissions;

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
