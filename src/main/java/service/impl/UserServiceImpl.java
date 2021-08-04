package service.impl;

import com.alibaba.druid.util.StringUtils;
import dao.UserDao;
import entity.User;
import org.springframework.stereotype.Service;
import service.UserService;
import util.Users;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public int add(User user) {
        Integer id = user.getId();
        if (user.getId() == null) {
            id = userDao.add(user);
            Users.put(user);
        } else {
            userDao.update(user);
            Users.update(user);
        }
        return id;
    }

    @Override
    public void delete(String account) {
        userDao.delete(account);
        Users.remove(account);
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

    /**
     * 启动放入内存
     */
    @PostConstruct
    private void initCache() {
        Users.init(userDao.findAll());
    }
}
