package com.fs11.tiner.servlet;

import sun.misc.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileServlet extends HttpServlet {
    private final String ASSETS_ROOT;

    public FileServlet(String assets_root) {
        ASSETS_ROOT = assets_root;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (ServletOutputStream os = resp.getOutputStream()) {
            String rqName = req.getRequestURI();
            ClassLoader classLoader = this.getClass().getClassLoader();

            byte[] bytes = IOUtils.readAllBytes(classLoader.getResourceAsStream(rqName.substring(1)));
            os.write(bytes);

        } catch (NullPointerException ex) {
            resp.setStatus(404);
        }
    }
}
