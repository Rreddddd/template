package dao;

import entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

    User findByAccount(String account);

    List<User> findAll();

    void add(User user);

    void update(User user);

    void delete(@Param("id") int id);

    User findById(@Param("id") int id);

    List<User> getWithPositionAll();

    User findWidthPositionById(int id);
}
