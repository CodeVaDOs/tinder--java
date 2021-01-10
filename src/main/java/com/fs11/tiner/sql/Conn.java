package com.fs11.tiner.sql;

import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class Conn {
    public static Optional<Connection> get(String url,
                                           String username,
                                           String password
    ) {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", username);
            properties.setProperty("password", password);
            properties.setProperty("autoReconnect", "true");

            return Optional.of(new Driver().connect(url, properties));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<Connection> get() {
        return get("jdbc:mariadb://185.67.2.16:3306/pswatch_stepDB",
                "pswatch_stepUser",
                "*fc4$kNNFXD5");
    }
}
