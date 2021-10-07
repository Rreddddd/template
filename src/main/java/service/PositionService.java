package service;

import entity.Position;

import java.util.List;

public interface PositionService {

    List<Position> getAll();

    List<Position> getWithUserAll();

    void add(Position position);

    void update(Position position);

    void delete(int id);
}
