package dao;

import entity.Position;

import java.util.List;

public interface PositionDao {

    List<Position> findByUserId(int userId);
}
