package de.jos.tselicence.core;

import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.core.util.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DbSetup {
    private final String database;
    private final IUserRepository userRepository;
    private final Environment env;
    private final String activeProfile;

    @Autowired
    public DbSetup(Environment env, @Qualifier(Comp.USER_REP) IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.env = env;
        this.database = this.env.getProperty("spring.datasource.url");
        this.activeProfile = env.getActiveProfiles()[0];
    }

    public void dbSetup() {
        createTable("request_logs_table.sql", "licences_table.sql", "users_table.sql", "licences_detail_table.sql");
        insertDefaultUser();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(database);
    }

    public void truncateTable(String tableName) {
        if (activeProfile.equals("dev")) {
            try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.executeUpdate("TRUNCATE TABLE " + tableName.trim() + " RESTART IDENTITY CASCADE;");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void dropTable(String... tableNames) {
        if (activeProfile.equals("dev")) {
            for (String table : tableNames) {
                try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                    st.executeUpdate("DROP TABLE " + table.trim() + ";");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void createTable(String... tables) {
        for (String path : tables) {
            try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.executeUpdate(Resource.get("sql/" + path).trim());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertDefaultUser() {
        userRepository.createUser(env.getProperty("custom.username"), env.getProperty("custom.password"), "admin");
    }
}
