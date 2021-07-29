package service;

import entity.MemberToken;

public interface MemberTokenService {

    void removeToken(String username);

    MemberToken getToken(String token);
}
