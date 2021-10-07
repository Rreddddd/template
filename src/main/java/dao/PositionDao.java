package dao;

import entity.Position;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface PositionDao {

    List<Position> findByUserId(int userId);

    List<Position> getAll();

    List<Position> getWidthUserAll();

    void add(Position position);

    void update(Position position);

    void delete(@Param("id") int id);
}
