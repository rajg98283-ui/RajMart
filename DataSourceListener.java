package com.raj.rajmart.listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Initializes HikariCP connection pool and runs schema + seed on startup.
 * Owns the full lifecycle of the DataSource (Singleton via ServletContext).
 */
@WebListener
public class DataSourceListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(DataSourceListener.class);
    public static final String DS_ATTR = "dataSource";

    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        try {
            HikariConfig config = new HikariConfig();
            // Embedded H2 for local / simple deploy. Switch to tcp for production server mode.
            String jdbcUrl = System.getenv().getOrDefault("JDBC_URL",
                    "jdbc:h2:mem:rajmart;DB_CLOSE_DELAY=-1;MODE=MySQL;DATABASE_TO_LOWER=TRUE");
            String user = System.getenv().getOrDefault("JDBC_USER", "sa");
            String pass = System.getenv().getOrDefault("JDBC_PASSWORD", "");

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(user);
            config.setPassword(pass);
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setPoolName("RajMartPool");
            config.setConnectionTimeout(10000);
            config.setIdleTimeout(300000);

            dataSource = new HikariDataSource(config);
            ctx.setAttribute(DS_ATTR, dataSource);
            log.info("HikariCP pool started: {}", jdbcUrl);

            // Initialize schema & seed
            runScript(dataSource, "/db/schema.sql");
            runScript(dataSource, "/db/seed.sql");
            log.info("Schema and seed data applied successfully");
        } catch (Exception e) {
            log.error("Failed to initialize DataSource", e);
            throw new RuntimeException("DataSource initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("HikariCP pool closed");
        }
    }

    private void runScript(DataSource ds, String resourcePath) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                log.warn("Script not found: {}", resourcePath);
                return;
            }
            String sql = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .filter(line -> !line.trim().startsWith("--") && !line.trim().isEmpty())
                    .collect(Collectors.joining("\n"));

            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement()) {
                // Split on ; for multi-statement
                for (String statement : sql.split(";")) {
                    String s = statement.trim();
                    if (!s.isEmpty()) {
                        stmt.execute(s);
                    }
                }
            }
        }
    }

    public static DataSource getDataSource(ServletContext ctx) {
        return (DataSource) ctx.getAttribute(DS_ATTR);
    }
}
