package service;

import entity.UserToken;

import java.util.Date;

public interface UserTokenService {

    void removeToken(String username);

    UserToken findValidBySeries(String series);

    void updateToken(String series, String tokenValue, Date lastUsed);

    void createNewToken(UserToken token);
}
