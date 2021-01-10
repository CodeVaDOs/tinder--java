package com.fs11.tiner.filter;

import lombok.SneakyThrows;
import com.fs11.tiner.util.Params;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class MessageFilter implements HttpFilter {
    @SneakyThrows
    @Override
    public void doHttpFilter(HttpServletRequest rq, HttpServletResponse rs, FilterChain chain) {
        String pathInfo = rq.getPathInfo();
        Optional<Integer> nullableId = Optional.ofNullable(pathInfo)
                .filter(pi -> pi.length() > 0)
                .map(pi -> pi.substring(1).split("/")[0])
                .flatMap(Params::toInt)
                .map(id -> {
                    rq.setAttribute("id", id);
                    return id;
                });

        if (nullableId.isPresent()) chain.doFilter(rq, rs);
        else {
            rs.setStatus(400);
            rs.sendRedirect("/users");
        }
    }
}
