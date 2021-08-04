package service;

import entity.User;

public interface UserService {

    int add(User user);

    void delete(String account);

    User findByAccount(String account);

    User findById(int id);

    int verifyAccountAndPassword(String account, String password);
}
