package security;

import entity.Module;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import security.voter.BaseVoter;
import util.Modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static final String[] IGNORE_PATTERN = {"/error/*", "/login", "/captcha", "/global/**", "/pc/css/*", "/pc/js/*", "/pc/image/*"};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequest().getServletPath();
        for (String pattern : IGNORE_PATTERN) {
            if (antPathMatcher.matchStart(pattern, requestUrl)) {
                return new ArrayList<>(Collections.singletonList(BaseVoter.IGNORE_ATTRIBUTE));
            }
        }
        Module module = Modules.get(requestUrl);
        if (module == null) {
            return new ArrayList<>(Collections.singletonList(BaseVoter.DEFAULT_ATTRIBUTE));
        }
        return SecurityConfig.createList(module.getId() + "");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
