package com.fs11.tiner.servlet;

import com.fs11.tiner.dao.UserDao;
import lombok.SneakyThrows;
import com.fs11.tiner.model.User;
import com.fs11.tiner.util.Params;
import com.fs11.tiner.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class UserServlet extends HttpServlet {
    private final TemplateEngine fm = TemplateEngine.resources("/templates");
    private final UserDao userDao;

    public UserServlet(UserDao userDao) {
        this.userDao = userDao;
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession(false).getAttribute("user");
        Optional<User> user = userDao.getUnvotedUser(sessionUser.getId());

        user.ifPresent((p) -> {
            HashMap<String, Object> data = new HashMap<>(2);

            data.put("id", p.getId());
            data.put("name", p.getName());
            data.put("imgUrl", p.getImgUrl());

            fm.render("like-page.ftl", data, resp);
        });

        if (!user.isPresent()) {
            try {
                resp.sendRedirect("/liked");
            } catch (IOException ignored) {
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Long> id = Params.getLongParam("id", req);
        User sessionUser = (User) req.getSession(false).getAttribute("user");

        Optional<String> vote = Params.getStrParam("vote", req);

        vote.map(Boolean::valueOf)
                .flatMap(v ->
                        id.map(i -> {
                                    userDao.vote(sessionUser.getId(), i, v);
                                    return i;
                                }
                        )
                );
        resp.sendRedirect("/users");
    }
}
