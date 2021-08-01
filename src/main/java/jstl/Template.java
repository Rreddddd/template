package jstl;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class Template extends BodyTagSupport {

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
        JspWriter out = pageContext.getOut();
        if (bodyContent != null) {
            if (out instanceof BodyContent) {
                out = ((BodyContent) out).getEnclosingWriter();
            }
            String content = this.bodyContent.getString();
            try {
                this.bodyContent.clear();
                out.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SKIP_PAGE;
    }
}
