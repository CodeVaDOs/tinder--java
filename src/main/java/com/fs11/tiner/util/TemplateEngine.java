package com.fs11.tiner.util;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class TemplateEngine {

    private final Configuration conf;


    private TemplateEngine() throws IOException {
        this.conf = new Configuration(Configuration.VERSION_2_3_29) {{
            setClassLoaderForTemplateLoading(TemplateEngine.class.getClassLoader(), "templates");
            setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
            setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            setLogTemplateExceptions(false);
            setWrapUncheckedExceptions(true);
        }};
    }

    public static TemplateEngine folder(final String path_from_project_root) {
        try {
            return new TemplateEngine();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    public static TemplateEngine resources(final String path_from_project_resources) {
        try {
//            String path = Paths
//                    .get(TemplateEngine.class.getClassLoader().getResource(path_from_project_resources).toURI())
//                    .toFile().getAbsolutePath();
            return new TemplateEngine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void render(String template, HashMap<String, Object> data, HttpServletResponse resp) {
        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

        try (PrintWriter w = resp.getWriter()) {
            conf.getTemplate(template).process(data, w);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException("Freemarker error", e);
        }
    }
}

