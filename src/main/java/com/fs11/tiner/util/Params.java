package com.fs11.tiner.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class Params {
    public static Optional<Integer> toInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException x) {
            return Optional.empty();
        }
    }

    public static Optional<Long> toLong(String s) {
        try {
            return Optional.of(Long.parseLong(s));
        } catch (NumberFormatException x) {
            return Optional.empty();
        }
    }

    public static Optional<String> getStrParam(String par, HttpServletRequest rq) {
        return Optional.ofNullable(rq.getParameter(par));
    }

    public static Optional<Integer> getIntParam(String par, HttpServletRequest rq) {
        return getStrParam(par, rq).flatMap(Params::toInt);
    }

    public static Optional<Long> getLongParam(String par, HttpServletRequest rq) {
        return getStrParam(par, rq).flatMap(Params::toLong);
    }
}

