package jstl;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class TemplatePlaceholder extends TagSupport {

    @Override
    public int doEndTag() {
        JspWriter out = pageContext.getOut();
        Object obj = this.pageContext.getRequest().getAttribute(id);
        try {
            if (obj != null) {
                out.write(obj.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
