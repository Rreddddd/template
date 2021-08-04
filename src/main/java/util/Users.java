package util;

import com.alibaba.druid.util.StringUtils;
import entity.User;
import exception.UserAccountEmptyException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Users {

    private static final Map<String, User> USER_MAP = new ConcurrentHashMap<>();

    private static String getAccount(User user) {
        String account = user.getAccount();
        ensureAccount(account);
        return account;
    }

    private static void ensureAccount(String account) {
        if (StringUtils.isEmpty(account)) {
            throw new UserAccountEmptyException("用户登录名不能为空");
        }
    }

    public static void init(Collection<User> users) {
        USER_MAP.clear();
        putAll(users);
    }

    public static void put(User user) {
        USER_MAP.put(getAccount(user), user);
    }

    public static void putAll(Collection<User> users) {
        if (users != null && users.size() > 0) {
            for (User user : users) {
                put(user);
            }
        }
    }

    public static void update(User user) {
        if (user != null) {
            User cacheUser = USER_MAP.get(getAccount(user));
            if (cacheUser != null) {
                synchronized (cacheUser) {
                    if (user.getId() != null) {
                        cacheUser.setId(user.getId());
                    }
                    if (user.getName() != null) {
                        cacheUser.setName(user.getName());
                    }
                    if (user.getPassword() != null) {
                        cacheUser.setPassword(user.getPassword());
                    }
                    cacheUser.setPhone(user.getPhone());
                    cacheUser.setEmail(user.getEmail());
                    cacheUser.setHeadImg(user.getHeadImg());
                    cacheUser.setFreeze(user.isFreeze());
                    cacheUser.setPosition(user.getPosition());
                }
            }
        }
    }

    public static void remove(String account) {
        ensureAccount(account);
        USER_MAP.remove(account);
    }

    public static User get(String account) {
        ensureAccount(account);
        User user = USER_MAP.get(account);
        return user == null ? null : user.copy();
    }
}
