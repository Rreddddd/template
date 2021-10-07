package service.impl;

import dao.PositionDao;
import dao.UserAndPositionDao;
import entity.Position;
import entity.User;
import entity.UserAndPosition;
import org.springframework.stereotype.Service;
import service.PositionService;
import util.Positions;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Resource
    private PositionDao positionDao;
    @Resource
    private UserAndPositionDao userAndPositionDao;
    @Resource
    private UserServiceImpl userService;

    @Override
    public List<Position> getAll() {
        return positionDao.getAll();
    }

    @Override
    public List<Position> getWithUserAll() {
        return positionDao.getWidthUserAll();
    }

    @Override
    public void add(Position position) {
        positionDao.add(position);
        List<User> users = position.getUsers();
        if (users != null && users.size() > 0) {
            List<UserAndPosition> userAndPositions = new ArrayList<>();
            for (User user : users) {
                userAndPositions.add(new UserAndPosition(user.getId(), position.getId()));
            }
            userAndPositionDao.addList(userAndPositions);
        }
        Positions.put(position);
    }

    @Override
    public void update(Position position) {
        positionDao.update(position);
        userAndPositionDao.removeByPositionId(position.getId());
        List<User> users = position.getUsers();
        if (users != null && users.size() > 0) {
            List<UserAndPosition> userAndPositions = new ArrayList<>();
            for (User user : users) {
                userAndPositions.add(new UserAndPosition(user.getId(), position.getId()));
            }
            userAndPositionDao.addList(userAndPositions);
        }
        Positions.update(position);
    }

    @Override
    public void delete(int id) {
        positionDao.delete(id);
        userAndPositionDao.removeByPositionId(id);
        Positions.remove(id);
    }

    /**
     * 启动放入内存
     */
    @PostConstruct
    private void initCache() {
        Positions.init(positionDao.getAll());
        userService.initCache();
    }
}
