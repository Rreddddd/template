package security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import util.Users;

import java.util.ArrayList;
import java.util.List;

public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        entity.User user = Users.get(account);
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new User(user.getAccount(), user.getPassword(), !user.isFreeze(), true, true, true, authorities);
    }
}
