package entity;

import util.SerialCloneAble;

import java.util.Objects;

public class Position extends SerialCloneAble {

    private Integer id;
    private String name;
    private boolean visible;

    public static final Position ADMIN_POSITION = new Position(1, "超级管理员", false);

    public Position() {
    }

    public Position(int id, String name, boolean visible) {
        this.id = id;
        this.name = name;
        this.visible = visible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(id, position.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
