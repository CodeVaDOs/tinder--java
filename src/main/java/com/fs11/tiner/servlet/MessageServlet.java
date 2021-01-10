package com.fs11.tiner.servlet;

import com.fs11.tiner.dao.MessageDao;
import com.fs11.tiner.dao.UserDao;
import com.fs11.tiner.model.Message;
import com.fs11.tiner.model.User;
import com.fs11.tiner.util.Params;
import com.fs11.tiner.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MessageServlet extends HttpServlet {
    private final TemplateEngine fm = TemplateEngine.resources("/templates");
    private final MessageDao messageDao;
    private final UserDao userDao;

    public MessageServlet(MessageDao messageDao, UserDao userDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession(false).getAttribute("user");

        Long from_id = sessionUser.getId();
        Long to_id = ((Integer) req.getAttribute("id")).longValue();

        Optional<User> to_user = userDao.findById(to_id);


        if (to_user.isPresent()) {
            List<Message> messages = messageDao.getMessages(to_id, from_id);
            HashMap<String, Object> data = new HashMap<>(3);
            data.put("messages", messages);
            data.put("user_from", from_id);
            data.put("user_to", to_user.get());

            fm.render("chat.ftl", data, resp);
        } else resp.sendRedirect("/users");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession(false).getAttribute("user");
        Long to_id = ((Integer) req.getAttribute("id")).longValue();

        Long from_id = sessionUser.getId();

        Optional<String> message_body = Params.getStrParam("message_body", req);


        Optional<Message> msg =
                message_body.flatMap(mb -> {
                            Message message = new Message();
                            message.setDate_in_millis(Instant.now().getEpochSecond());
                            message.setFrom_id(from_id);
                            message.setTo_id(to_id);
                            message.setMessage_body(mb);

                            return messageDao.save(message);
                        }
                );

        resp.sendRedirect(String.format("/messages/%d", to_id));
    }
}
