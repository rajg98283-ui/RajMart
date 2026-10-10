package com.raj.rajmart.controller;

import com.raj.rajmart.dao.JdbcProductDAO;
import com.raj.rajmart.listener.DataSourceListener;
import com.raj.rajmart.model.Product;
import com.raj.rajmart.model.User;
import com.raj.rajmart.service.ProductService;
import com.raj.rajmart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/api/v1/products/*", "/products", "/product"})
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        DataSource ds = DataSourceListener.getDataSource(getServletContext());
        productService = new ProductService(new JdbcProductDAO(ds));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        boolean isApi = path.startsWith("/api/");

        if (path.matches(".*/products/?") || path.equals("/products")) {
            String keyword = req.getParameter("q");
            String category = req.getParameter("category");
            List<Product> products = productService.search(keyword, category);
            if (isApi) {
                JsonUtil.writeSuccess(resp, products);
            } else {
                req.setAttribute("products", products);
                req.getRequestDispatcher("/products.jsp").forward(req, resp);
            }
            return;
        }

        // single product by id: /api/v1/products/123 or /product?id=123
        String idParam = req.getParameter("id");
        if (idParam == null && path.contains("/products/")) {
            String[] parts = path.split("/");
            idParam = parts[parts.length - 1];
        }
        if (idParam != null) {
            try {
                long id = Long.parseLong(idParam);
                Optional<Product> p = productService.getById(id);
                if (p.isPresent()) {
                    if (isApi) {
                        JsonUtil.writeSuccess(resp, p.get());
                    } else {
                        req.setAttribute("product", p.get());
                        req.getRequestDispatcher("/product-detail.jsp").forward(req, resp);
                    }
                } else {
                    if (isApi) {
                        JsonUtil.writeError(resp, 404, "NOT_FOUND", "Product not found");
                    } else {
                        resp.sendError(404);
                    }
                }
            } catch (NumberFormatException e) {
                JsonUtil.writeError(resp, 400, "BAD_REQUEST", "Invalid product id");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || (user.getRole() != User.Role.SELLER && user.getRole() != User.Role.ADMIN)) {
            JsonUtil.writeError(resp, 403, "FORBIDDEN", "Seller access required");
            return;
        }
        try {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            BigDecimal price = new BigDecimal(req.getParameter("price"));
            int stock = Integer.parseInt(req.getParameter("stockQty"));
            String category = req.getParameter("category");
            String imageUrl = req.getParameter("imageUrl");
            long id = productService.create(user.getId(), name, description, price, stock, category, imageUrl);
            JsonUtil.writeSuccess(resp, 201, java.util.Map.of("id", id));
        } catch (Exception e) {
            JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", e.getMessage());
        }
    }
}
