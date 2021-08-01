package dao;

import entity.UserToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserTokenDao {

    void removeToken(String username);

    UserToken findValidBySeries(String series);

    void updateToken(@Param("series") String series, @Param("token") String token, @Param("lastUsedTime") Date lastUsedTime);

    void createNewToken(UserToken token);
}
