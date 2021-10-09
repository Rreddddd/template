package service.impl;

import com.alibaba.druid.util.StringUtils;
import dao.UserAndPositionDao;
import dao.UserDao;
import entity.Position;
import entity.User;
import entity.UserAndPosition;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import security.MyDelegatingPasswordEncoder;
import service.UserService;
import service.UserTokenService;
import util.Users;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserAndPositionDao userAndPositionDao;
    @Resource
    private MyDelegatingPasswordEncoder passwordEncoder;
    @Resource
    private SessionRegistry sessionRegistry;
    @Resource
    private UserTokenService userTokenService;

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
        if (user.isFreeze()) {
            freeze(user);
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
            //清空token
            userTokenService.removeToken(user.getName());
            Users.remove(user.getAccount());
            if (user.isFreeze()) {
                freeze(user);
            }
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
     * @return 0无此账户 1密码错误 2成功 3冻结
     */
    @Override
    public int verifyAccountAndPassword(String account, String password) {
        User user = findByAccount(account);
        if (user == null) {
            return 0;
        }
        if (user.isFreeze()) {
            return 3;
        }
        password = passwordEncoder.encode(password);
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

    @Override
    @Transactional
    public void freeze(int id, boolean freeze) {
        userDao.freeze(id, freeze);
        User user = userDao.findWidthPositionById(id);
        Users.update(user);
        if (freeze) {
            //先清空token
            userTokenService.removeToken(user.getName());
            freeze(user);
        }
    }

    /**
     * 冻结/删除 用户，删除session信息
     *
     * @param user 人员
     */
    private void freeze(User user) {
        List<Object> users = sessionRegistry.getAllPrincipals(); // 获取session中所有的用户信息
        for (Object principal : users) {
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User loggedUser = (org.springframework.security.core.userdetails.User) principal;
                if (user.getAccount().equals(loggedUser.getUsername())) {
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false); // false代表不包含过期session
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                        }
                    }
                }
            }
        }
    }

    /**
     * 启动放入内存 在职位后放入
     */
    void initCache() {
        Users.init(userDao.getWithPositionAll());
    }
}
