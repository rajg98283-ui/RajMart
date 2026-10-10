package com.raj.rajmart.controller;

import com.raj.rajmart.listener.DataSourceListener;
import com.raj.rajmart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        String db = "DOWN";
        try {
            DataSource ds = DataSourceListener.getDataSource(getServletContext());
            if (ds != null) {
                try (Connection c = ds.getConnection()) {
                    if (c.isValid(2)) {
                        db = "UP";
                    }
                }
            }
        } catch (Exception ignored) {
        }
        status.put("db", db);
        JsonUtil.writeSuccess(resp, status);
    }
}
