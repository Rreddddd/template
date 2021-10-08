package service;

import entity.Position;
import entity.User;

import java.util.List;

public interface UserService {

    int add(User user);

    void deleteByAccount(String account);

    void delete(int id);

    User findByAccount(String account);

    User findById(int id);

    User findWidthPositionById(int id);

    int verifyAccountAndPassword(String account, String password);

    List<User> getWithPositionAll();

    void update(User user);

    void freeze(int id, boolean freeze);
}
