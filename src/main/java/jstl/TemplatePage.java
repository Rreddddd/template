package jstl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TemplatePage extends BodyTagSupport {

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
        CustomResponse bufferedResponse = new CustomResponse((HttpServletResponse) this.pageContext.getResponse());
        try {
            this.pageContext.getServletContext().getRequestDispatcher("/pc/jsp/template.jsp").include(this.pageContext.getRequest(), bufferedResponse);
            out.write(bufferedResponse.getContent());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        return SKIP_PAGE;
    }

    private static class CustomResponse extends HttpServletResponseWrapper {

        private final StringWriter strWriter = new StringWriter();
        private final PrintWriter out = new PrintWriter(strWriter);

        public CustomResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() {
            return out;
        }

        public String getContent() {
            return strWriter.toString();
        }
    }
}
