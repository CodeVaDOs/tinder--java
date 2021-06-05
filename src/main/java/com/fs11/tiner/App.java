package com.fs11.tiner;

import com.fs11.tiner.dao.MessageDao;
import com.fs11.tiner.dao.UserDao;
import com.fs11.tiner.filter.LoginFilter;
import com.fs11.tiner.filter.MessageFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.fs11.tiner.sql.Conn;

import com.fs11.tiner.servlet.*;

import javax.servlet.DispatcherType;
import java.sql.Connection;
import java.util.EnumSet;
import java.util.Optional;

public class App {
    public static void main(String[] args) throws Exception {
        String portStr = System.getenv("PORT");
        Integer port = Integer.parseInt(portStr);
        System.out.println("PORT: " + port);
        Server server = new Server(port);
        ServletContextHandler handler = new ServletContextHandler();
        final Optional<Connection> connection = Conn.get();
        final UserDao userDao = new UserDao(connection);
        final MessageDao messageDao = new MessageDao(connection);

        SessionHandler sessionHandler = new SessionHandler();
        handler.setSessionHandler(sessionHandler);


        handler.addServlet(new ServletHolder(new LoginServlet(userDao)), "/login");
        handler.addServlet(new ServletHolder(new FileServlet("/")), "/assets/*");
        handler.addFilter(new FilterHolder(new LoginFilter(connection)), "/*", EnumSet.of(DispatcherType.REQUEST));

        handler.addServlet(new ServletHolder(new UserServlet(userDao)), "/users");

        handler.addServlet(new ServletHolder(new LikeServlet(userDao)), "/liked");

        handler.addServlet(new ServletHolder(new MessageServlet(messageDao, userDao)), "/messages/*");
        handler.addFilter(MessageFilter.class, "/messages/*", EnumSet.of(DispatcherType.REQUEST));


        handler.addServlet(RedirectServlet.class, "/*");

        server.setHandler(handler);
        server.start();
        server.join();

//        jdbc:mariadb://185.67.2.16:3306/pswatch_stepDB
//        pswatch_stepUser
//        *fc4$kNNFXD5
    }
}
