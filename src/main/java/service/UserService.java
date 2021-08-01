package service;

import entity.User;

public interface UserService {

    int add(User user);

    void delete(String account);

    User findByAccount(String account);

    int verifyAccountAndPassword(String account, String password);
}
