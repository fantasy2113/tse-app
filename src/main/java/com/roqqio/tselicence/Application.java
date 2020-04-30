package com.roqqio.tselicence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


@SpringBootApplication
@EnableJpaAuditing
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    private static void createDatabase(String... args) throws Exception {
        final String delimiter = ":=";
        String dbUrl = null;
        String db = null;
        for (String arg : args) {
            System.out.println(arg);
            if (arg.contains("db_password" + delimiter)) {
                dbUrl = "jdbc:postgresql://localhost:5432/postgres?user=dev&password=<password>".replace("<password>", arg.split(delimiter)[1]);
            }
            if (arg.contains("db_name" + delimiter)) {
                db = arg.split(delimiter)[1];
            }
        }
        if (dbUrl != null && db != null) {
            try (Connection conn = DriverManager.getConnection(dbUrl); Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE DATABASE " + db + ";");
            }
        }
    }
}
