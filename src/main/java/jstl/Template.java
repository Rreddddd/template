package jstl;

import util.Context;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class Template extends BodyTagSupport {

    @Override
    public int doEndTag() throws JspException {
        try {
            ServletContext servletContext = this.pageContext.getRequest().getServletContext();
            servletContext.setAttribute("html", this.bodyContent.getString());
            Context.getUser();
            this.pageContext.include("/pc/jsp/template.jsp");
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }
}
