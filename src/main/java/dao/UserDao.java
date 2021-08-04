package dao;

import entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    User findByAccount(String account);

    List<User> findAll();

    Integer add(User user);

    void update(User user);

    void delete(String account);

    User findById(int id);
}
