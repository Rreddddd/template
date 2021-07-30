package dao;

import entity.UserToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface UserTokenDao {

    void removeToken(String username);

    UserToken findValidBySeries(String series);

    void updateToken(String series, String token, Date lastUsedTime);

    void createNewToken(UserToken token);
}
