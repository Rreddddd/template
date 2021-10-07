package entity;

public class UserAndPosition {

    private int userId;
    private int positionId;

    public UserAndPosition() {
    }

    public UserAndPosition(int userId, int positionId) {
        this.userId = userId;
        this.positionId = positionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }
}
