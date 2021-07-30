package security;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import service.MemberTokenService;

import javax.annotation.Resource;
import java.util.Date;

public class MyPersistentTokenRepository implements PersistentTokenRepository {

    @Resource
    private MemberTokenService memberTokenService;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        System.out.println("");
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        System.out.println("");
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        memberTokenService.removeToken(username);
    }
}
