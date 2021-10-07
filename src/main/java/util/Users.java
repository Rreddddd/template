package util;

import com.alibaba.druid.util.StringUtils;
import entity.Position;
import entity.User;
import exception.UserAccountEmptyException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Users {

    private static final Map<String, InnerUser> USER_MAP = new ConcurrentHashMap<>();

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
        user.setPositions(getCachePositions(user.getPositions()));
        USER_MAP.put(getAccount(user), new InnerUser(user));
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
            InnerUser innerUser = USER_MAP.get(getAccount(user));
            User cacheUser = innerUser.user;
            if (cacheUser != null) {
                synchronized (cacheUser) {
                    if (user.getId() != null) {
                        cacheUser.setId(user.getId());
                    }
                    if (user.getName() != null) {
                        cacheUser.setName(user.getName());
                        innerUser.formatPinyin();
                    }
                    if (user.getPassword() != null) {
                        cacheUser.setPassword(user.getPassword());
                    }
                    cacheUser.setPhone(user.getPhone());
                    cacheUser.setEmail(user.getEmail());
                    cacheUser.setHeadImg(user.getHeadImg());
                    cacheUser.setFreeze(user.isFreeze());
                    cacheUser.setPositions(getCachePositions(user.getPositions()));
                }
            }
        }
    }

    private static List<Position> getCachePositions(List<Position> positions) {
        List<Position> cachePositions = new ArrayList<>();
        if (positions != null && positions.size() > 0) {
            for (Position position : positions) {
                cachePositions.add(Positions.get(position.getId()));
            }
        }
        return cachePositions;
    }

    public static void remove(String account) {
        ensureAccount(account);
        USER_MAP.remove(account);
    }

    public static User get(String account) {
        ensureAccount(account);
        InnerUser innerUser = USER_MAP.get(account);
        return innerUser == null ? null : (User) innerUser.user.clone();
    }

    public static List<User> search(String searchKey) {
        searchKey = searchKey.toLowerCase(Locale.ROOT);
        Set<InnerUser> searchUsers = new TreeSet<>(Comparator.comparing(o -> o.fullPinyin));
        Collection<InnerUser> users = USER_MAP.values();
        if (util.StringUtils.isBlank(searchKey)) {
            searchUsers.addAll(users);
        } else {
            for (InnerUser user : users) {
                if (user.user.getName().contains(searchKey) || user.sortPinyin.contains(searchKey) || user.fullPinyin.contains(searchKey)) {
                    searchUsers.add(user);
                }
            }
        }
        List<User> result = new ArrayList<>();
        for (InnerUser innerUser : searchUsers) {
            result.add((User) innerUser.user.clone());
        }
        return result;
    }

    private static class InnerUser {

        User user;
        String sortPinyin;
        String fullPinyin;

        InnerUser(User user) {
            this.user = user;
            formatPinyin();
        }

        void formatPinyin() {
            this.sortPinyin = PinyinTools.getFirstSpell(user.getName()).toLowerCase(Locale.ROOT);
            this.fullPinyin = PinyinTools.getFullSpell(user.getName()).toLowerCase(Locale.ROOT);
        }
    }
}
