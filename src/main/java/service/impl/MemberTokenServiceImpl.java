package service.impl;

import dao.MemberTokenDao;
import entity.MemberToken;
import org.springframework.stereotype.Service;
import service.MemberTokenService;

import javax.annotation.Resource;

@Service
public class MemberTokenServiceImpl implements MemberTokenService {

    @Resource
    private MemberTokenDao memberTokenDao;

    @Override
    public void removeToken(String username) {
        memberTokenDao.removeToken(username);
    }

    @Override
    public MemberToken getToken(String token) {
        return memberTokenDao.getToken(token);
    }
}
