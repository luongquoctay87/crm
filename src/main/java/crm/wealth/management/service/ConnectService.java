package crm.wealth.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
@Slf4j
public class ConnectService {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public Connection getConnection(String schema) {
        Connection connection = null;
        try {
            String path = url;
            Class.forName("org.postgresql.Driver");
            if (StringUtils.hasLength(schema)) {
                path = url + "?currentSchema=" + schema;
            }
            connection = DriverManager.getConnection(path, username, password);
            log.info("DB is connected");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            log.error("DB connect fail, url={}", url);
            return null;
        }
    }
}
