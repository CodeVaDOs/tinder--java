package com.fs11.tiner.filter;

import com.fs11.tiner.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;

public class LoginFilter implements HttpFilter {
    private final Optional<Connection> connection;


    public LoginFilter(Optional<Connection> connection) {
        this.connection = connection;
    }

    @Override
    public void doHttpFilter(HttpServletRequest rq, HttpServletResponse rs, FilterChain chain) throws IOException, ServletException {
        HttpSession session = rq.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        String loginURL = rq.getContextPath() + "/login";

        if (rq.getPathInfo() != null && rq.getPathInfo().split("/").length > 1 && rq.getPathInfo().split("/")[1].equals("css")) {
            chain.doFilter(rq, rs);
        } else if (user == null && !rq.getRequestURI().equals(loginURL)) {
            rs.sendRedirect(loginURL);
        } else {
            chain.doFilter(rq, rs);
        }
    }
}