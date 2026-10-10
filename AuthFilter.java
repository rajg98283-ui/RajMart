package com.raj.rajmart.filter;

import com.raj.rajmart.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Protects authenticated routes. Public paths are allowed through.
 */
@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private static final String[] PUBLIC_PREFIXES = {
            "/login", "/register", "/css/", "/js/", "/images/", "/api/v1/health",
            "/api/v1/products", "/api/v1/auth"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // always allow static and public
        if (isPublic(path) || path.equals("/") || path.equals("/index.jsp") || path.equals("/home")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            if (path.startsWith("/api/")) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\":false,\"error\":{\"code\":\"UNAUTHORIZED\",\"message\":\"Login required\"}}");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            }
            return;
        }

        // role checks for admin / seller panels
        if (path.startsWith("/admin") && user.getRole() != User.Role.ADMIN) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if ((path.startsWith("/seller") || path.startsWith("/api/v1/seller"))
                && user.getRole() != User.Role.SELLER && user.getRole() != User.Role.ADMIN) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        for (String p : PUBLIC_PREFIXES) {
            if (path.startsWith(p)) {
                return true;
            }
        }
        return false;
    }
}
