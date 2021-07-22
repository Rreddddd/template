package service;

import entity.User;

public interface UserService {

    User findByAccount(String account);

    int verifyAccountAndPassword(String account, String password);
}
