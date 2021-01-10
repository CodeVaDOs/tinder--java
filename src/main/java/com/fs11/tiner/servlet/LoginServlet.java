package com.fs11.tiner.servlet;

import com.fs11.tiner.dao.UserDao;
import com.fs11.tiner.model.User;
import com.fs11.tiner.util.Params;
import com.fs11.tiner.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class LoginServlet extends HttpServlet {
    private final TemplateEngine fm = TemplateEngine.resources("/templates");
    private final UserDao userDao;

    public LoginServlet(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fm.render("login.ftl", new HashMap<>(1), resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> login = Params.getStrParam("name", req);
        Optional<String> password = Params.getStrParam("password", req);


        Optional<User> loggedUser = login.flatMap(l ->
                password.flatMap(p -> {
                    Optional<User> user = userDao.findByLogin(l);
                    return user.filter(u -> u.getPassword().equals(p))
                            .map(u -> {
                                req.getSession().setAttribute("user", u);
                                return u;
                            });
                })
        );
        if (loggedUser.isPresent()) resp.sendRedirect("/users");
        else resp.sendRedirect("/login");
    }
}
