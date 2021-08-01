package entity;

import java.util.Objects;

public class Permission {

    private Integer id;
    private int authId;
    private int menuId;
    private int type;

    public Permission() {
    }

    public Permission(int authId, int type) {
        this.authId = authId;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return authId == that.authId && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, type);
    }
}
