package entity;

public enum MemberType {

    USER(0, "用户"),
    DEPT(1, "部门");

    private int type;
    private String name;

    MemberType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
