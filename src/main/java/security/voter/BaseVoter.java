package security.voter;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.ArrayList;
import java.util.Collection;

public class BaseVoter implements AccessDecisionVoter<FilterInvocation> {

    private static final String DEFAULT_SECURITY_USER_NAME = "anonymousUser";
    public static final ConfigAttribute IGNORE_ATTRIBUTE = new SecurityConfig("-1");
    public static final ConfigAttribute DEFAULT_ATTRIBUTE = new SecurityConfig("0");

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
        ConfigAttribute configAttribute = ((ArrayList<ConfigAttribute>) attributes).get(0);
        if (configAttribute.equals(IGNORE_ATTRIBUTE)) {
            return ACCESS_GRANTED;
        }
        if (configAttribute.equals(DEFAULT_ATTRIBUTE)) {
            return authentication.getName().equals(DEFAULT_SECURITY_USER_NAME) ? ACCESS_DENIED : ACCESS_GRANTED;
        }
        return ACCESS_ABSTAIN;
    }
}