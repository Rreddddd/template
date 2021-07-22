package service.impl;

import com.alibaba.druid.util.StringUtils;
import dao.UserDao;
import entity.User;
import org.springframework.stereotype.Service;
import service.UserService;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User findByAccount(String account) {
        if (StringUtils.isEmpty(account)) {
            return null;
        }
        return userDao.findByAccount(account.trim());
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
}
