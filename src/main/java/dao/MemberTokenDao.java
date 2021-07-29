package dao;

import entity.MemberToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberTokenDao {

    void removeToken(String username);

    MemberToken getToken(String token);
}
