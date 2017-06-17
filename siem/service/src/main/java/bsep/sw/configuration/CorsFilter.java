package bsep.sw.configuration;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static bsep.sw.util.HttpHeadersUtil.*;

/**
 * CORS Filter settings.
 */
@Component
public class CorsFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Standard init - must override
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {

        final HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN.getName(), ACCESS_CONTROL_ALLOW_ORIGIN.getValue());
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS.getName(), ACCESS_CONTROL_ALLOW_METHODS.getValue());
        response.setHeader(ACCESS_CONTROL_MAX_AGE.getName(), ACCESS_CONTROL_MAX_AGE.getValue());
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS.getName(), ACCESS_CONTROL_ALLOW_HEADERS.getValue());
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS.getName(), ACCESS_CONTROL_EXPOSE_HEADERS.getValue());
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Standard destroy - must override
    }
}
