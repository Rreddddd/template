package mvc;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import util.Context;

import java.util.Locale;

public class MyInternalResourceViewResolver extends InternalResourceViewResolver {

    /**
     * 网站类型 0:pc/mobile 1:只有pc 2:只有mobile
     */
    private int status;

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (!viewName.startsWith("redirect:") && !viewName.startsWith("/pc/") && !viewName.startsWith("/mobile/")) {
            if (status == 0) {
                if (Context.isPhone()) {
                    viewName = "/mobile" + viewName;
                } else {
                    viewName = "/pc" + viewName;
                }
            } else if (status == 1) {
                viewName = "/pc" + viewName;
            } else if (status == 2) {
                viewName = "/mobile" + viewName;
            }
        }
        return super.resolveViewName(viewName, locale);
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
