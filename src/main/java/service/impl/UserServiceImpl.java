package service.impl;

import com.alibaba.druid.util.StringUtils;
import dao.UserAndPositionDao;
import dao.UserDao;
import entity.Position;
import entity.User;
import entity.UserAndPosition;
import org.springframework.stereotype.Service;
import service.UserService;
import util.Users;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserAndPositionDao userAndPositionDao;

    @Override
    public int add(User user) {
        if (user.getId() == null) {
            userDao.add(user);
            Users.put(user);
        } else {
            userDao.update(user);
            Users.update(user);
        }
        userAndPositionDao.removeByUserId(user.getId());
        List<Position> positions = user.getPositions();
        if (positions != null && positions.size() > 0) {
            List<UserAndPosition> userAndPositions = new ArrayList<>();
            for (Position position : positions) {
                userAndPositions.add(new UserAndPosition(user.getId(), position.getId()));
            }
            userAndPositionDao.addList(userAndPositions);
        }
        return user.getId();
    }

    @Override
    public void deleteByAccount(String account) {

    }

    @Override
    public void delete(int id) {
        User user = findById(id);
        if (user != null) {
            userDao.delete(id);
            Users.remove(user.getAccount());
        }
    }

    @Override
    public User findByAccount(String account) {
        if (StringUtils.isEmpty(account)) {
            return null;
        }
        return userDao.findByAccount(account.trim());
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findWidthPositionById(int id) {
        return userDao.findWidthPositionById(id);
    }

    /**
     * @param account  用户名
     * @param password 密码
     * @return 0无此账户 1密码错误 2成功
     */
    @Override
    public int verifyAccountAndPassword(String account, String password) {
        User user = findByAccount(account);
        if (user == null) {
            return 0;
        }
        return user.getPassword().equals(password) ? 2 : 1;
    }

    @Override
    public List<User> getWithPositionAll() {
        return userDao.getWithPositionAll();
    }

    @Override
    public void update(User user) {
        User oldUser = findWidthPositionById(user.getId());
        user.setPositions(oldUser.getPositions());
        add(user);
    }

    /**
     * 启动放入内存 在职位后放入
     */
    void initCache() {
        Users.init(userDao.getWithPositionAll());
    }
}
