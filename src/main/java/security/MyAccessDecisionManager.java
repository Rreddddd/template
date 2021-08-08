package security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.List;

public class MyAccessDecisionManager extends AffirmativeBased {

    private final AccessDecisionVoter<FilterInvocation> baseDecisionVoter;

    protected MyAccessDecisionManager(AccessDecisionVoter<FilterInvocation> baseDecisionVoter, List<AccessDecisionVoter<?>> decisionVoters) {
        super(decisionVoters);
        this.baseDecisionVoter = baseDecisionVoter;
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (baseDecisionVoter != null) {
            int result = baseDecisionVoter.vote(authentication, (FilterInvocation) object, configAttributes);
            if (result == AccessDecisionVoter.ACCESS_GRANTED) {
                return;
            }
            if (result == AccessDecisionVoter.ACCESS_DENIED) {
                throw new AccessDeniedException(messages.getMessage(
                        "MyAccessDecisionManager.accessDenied", "Access is denied"));
            }
        }
        super.decide(authentication, object, configAttributes);
    }
}
