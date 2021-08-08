package entity;

import java.util.Objects;

public class Permission {

    private Integer id;
    private PermissionId pid;
    private int menuId;

    public Permission() {
    }

    public Permission(PermissionId id) {
        this.pid = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PermissionId getPid() {
        return pid;
    }

    public void setPid(PermissionId pid) {
        this.pid = pid;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
