package com.raj.rajmart.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JSON serialization helpers and standard response envelope writer.
 */
public final class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class,
                    (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();

    private JsonUtil() {
    }

    public static Gson gson() {
        return GSON;
    }

    public static void writeSuccess(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        JsonObject envelope = new JsonObject();
        envelope.addProperty("success", true);
        envelope.add("data", GSON.toJsonTree(data));
        envelope.add("error", null);
        try (PrintWriter out = resp.getWriter()) {
            out.print(GSON.toJson(envelope));
        }
    }

    public static void writeSuccess(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        JsonObject envelope = new JsonObject();
        envelope.addProperty("success", true);
        envelope.add("data", GSON.toJsonTree(data));
        envelope.add("error", null);
        try (PrintWriter out = resp.getWriter()) {
            out.print(GSON.toJson(envelope));
        }
    }

    public static void writeError(HttpServletResponse resp, int status, String code, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        JsonObject error = new JsonObject();
        error.addProperty("code", code);
        error.addProperty("message", message);
        JsonObject envelope = new JsonObject();
        envelope.addProperty("success", false);
        envelope.add("data", null);
        envelope.add("error", error);
        try (PrintWriter out = resp.getWriter()) {
            out.print(GSON.toJson(envelope));
        }
    }
}
