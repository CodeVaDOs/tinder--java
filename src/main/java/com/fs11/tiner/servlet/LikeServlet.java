package com.fs11.tiner.servlet;

import com.fs11.tiner.dao.UserDao;
import com.fs11.tiner.model.User;
import com.fs11.tiner.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LikeServlet extends HttpServlet {
    private final TemplateEngine fm = TemplateEngine.resources("/templates");
    private final UserDao userDao;

    public LikeServlet(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession(false).getAttribute("user");

        HashMap<String, Object> data = new HashMap<>(1);
        List<User> liked = userDao.getLiked(sessionUser.getId());

        data.put("liked", liked);

        fm.render("liked-list.ftl", data, resp);
    }
}
