package entity;

import java.util.Objects;

public class PermissionId {

    private int authId;
    private int type;

    public static final int USER_TYPE = 0;
    public static final int POSITION_TYPE = 1;

    public PermissionId() {
    }

    public PermissionId(int authId, int type) {
        this.authId = authId;
        this.type = type;
    }

    public static PermissionId createUserId(int userId) {
        return new PermissionId(userId, USER_TYPE);
    }

    public static PermissionId createPositionId(int positionId) {
        return new PermissionId(positionId, POSITION_TYPE);
    }

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
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
        PermissionId that = (PermissionId) o;
        return authId == that.authId && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, type);
    }
}
