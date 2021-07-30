package service.impl;

import dao.UserTokenDao;
import entity.UserToken;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;
import service.UserTokenService;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Resource
    private UserTokenDao userTokenDao;

    @Override
    public void removeToken(String username) {
        userTokenDao.removeToken(username);
    }

    @Override
    public UserToken findValidBySeries(String series) {
        return userTokenDao.findValidBySeries(series);
    }

    @Override
    public void updateToken(String series, String token, Date lastUsed) {
        userTokenDao.updateToken(series, token, lastUsed);
    }

    @Override
    public void createNewToken(UserToken token) {
        userTokenDao.createNewToken(token);
    }
}
