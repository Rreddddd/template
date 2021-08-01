package jstl;

import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class TemplateContent extends BodyTagSupport {

    @Override
    public int doAfterBody() {
        return SKIP_BODY;
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() {
        String content = this.bodyContent.getString();
        try {
            this.pageContext.getRequest().setAttribute(id, content);
            this.bodyContent.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
