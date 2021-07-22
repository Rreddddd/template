package dao;

import entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    User findByAccount(String account);
}
