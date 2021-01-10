package com.fs11.tiner.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HttpFilter extends Filter {
    @Override
    default void init(FilterConfig filterConfig) throws ServletException { }

    void doHttpFilter(HttpServletRequest rq, HttpServletResponse rs, FilterChain chain) throws IOException, ServletException;

    @Override
    default void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest &&
                response instanceof HttpServletResponse) {
            HttpServletRequest rq = (HttpServletRequest) request;
            HttpServletResponse rs = (HttpServletResponse) response;
            doHttpFilter(rq, rs, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    default void destroy() {

    }
}
