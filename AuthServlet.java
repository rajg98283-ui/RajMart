package com.raj.rajmart.controller;

import com.raj.rajmart.dao.JdbcUserDAO;
import com.raj.rajmart.listener.DataSourceListener;
import com.raj.rajmart.model.User;
import com.raj.rajmart.service.AuthService;
import com.raj.rajmart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(urlPatterns = {"/api/v1/auth/*", "/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        DataSource ds = DataSourceListener.getDataSource(getServletContext());
        authService = new AuthService(new JdbcUserDAO(ds));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.equals("/logout")) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (path.equals("/login") || path.endsWith("/login.jsp")) {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }
        if (path.equals("/register") || path.endsWith("/register.jsp")) {
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.endsWith("/login") || path.equals("/login")) {
            handleLogin(req, resp);
        } else if (path.endsWith("/register") || path.equals("/register")) {
            handleRegister(req, resp);
        } else {
            JsonUtil.writeError(resp, 404, "NOT_FOUND", "Unknown auth endpoint");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        boolean isApi = req.getRequestURI().contains("/api/");

        Optional<User> userOpt = authService.login(email, password);
        if (userOpt.isEmpty()) {
            if (isApi) {
                JsonUtil.writeError(resp, 401, "INVALID_CREDENTIALS", "Invalid email or password");
            } else {
                req.setAttribute("error", "Invalid email or password");
                try {
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                } catch (ServletException e) {
                    throw new IOException(e);
                }
            }
            return;
        }

        User user = userOpt.get();
        // regenerate session ID on login
        HttpSession old = req.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30 * 60); // 30 min

        if (isApi) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("name", user.getName());
            data.put("email", user.getEmail());
            data.put("role", user.getRole().name());
            JsonUtil.writeSuccess(resp, data);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String roleStr = req.getParameter("role");
        boolean isApi = req.getRequestURI().contains("/api/");

        User.Role role = User.Role.BUYER;
        if ("SELLER".equalsIgnoreCase(roleStr)) {
            role = User.Role.SELLER;
        }

        try {
            long id = authService.register(name, email, password, role);
            if (isApi) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("message", "Registration successful");
                JsonUtil.writeSuccess(resp, 201, data);
            } else {
                resp.sendRedirect(req.getContextPath() + "/login.jsp?registered=1");
            }
        } catch (IllegalArgumentException e) {
            if (isApi) {
                JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", e.getMessage());
            } else {
                req.setAttribute("error", e.getMessage());
                try {
                    req.getRequestDispatcher("/register.jsp").forward(req, resp);
                } catch (ServletException ex) {
                    throw new IOException(ex);
                }
            }
        }
    }
}
