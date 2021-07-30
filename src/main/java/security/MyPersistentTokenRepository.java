package security;

import entity.UserToken;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import service.UserTokenService;

import javax.annotation.Resource;
import java.util.Date;

public class MyPersistentTokenRepository implements PersistentTokenRepository {

    @Resource
    private UserTokenService userTokenService;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        userTokenService.createNewToken(new UserToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate(), new Date(), true));
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        userTokenService.updateToken(series, tokenValue, lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        UserToken userToken = userTokenService.findValidBySeries(series);
        if (userToken == null) {
            return null;
        }
        return new PersistentRememberMeToken(userToken.getUsername(), userToken.getSeries(), userToken.getToken(), userToken.getLastUsedTime());
    }

    @Override
    public void removeUserTokens(String username) {
        userTokenService.removeToken(username);
    }
}
