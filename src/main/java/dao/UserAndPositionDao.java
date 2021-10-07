package dao;

import entity.UserAndPosition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAndPositionDao {

    void addList(@Param("userAndPositions") List<UserAndPosition> userAndPositions);

    void removeByPositionId(@Param("positionId") int positionId);

    void removeByUserId(@Param("userId") int userId);
}
