package security.voter;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import util.Menus;
import util.Users;

import java.util.ArrayList;
import java.util.Collection;

public class UserAssignVoter implements AccessDecisionVoter<FilterInvocation> {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
        ConfigAttribute configAttribute = ((ArrayList<ConfigAttribute>) attributes).get(0);
        Menus.InnerMenu innerMenu = Menus.getInnerByModuleId(Integer.parseInt(configAttribute.getAttribute()));
        if (innerMenu != null && innerMenu.checkPermission(Users.get(authentication.getName()))) {
            return ACCESS_GRANTED;
        }
        return ACCESS_ABSTAIN;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
