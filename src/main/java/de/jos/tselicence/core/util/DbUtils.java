package de.jos.tselicence.core.util;

import de.jos.tselicence.config.Context;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DbUtils {

    private DbUtils() {
    }

    private static Connection getConnection() throws SQLException {
        Environment env = Context.getBean(Environment.class);
        return DriverManager.getConnection(env.getProperty("spring.datasource.url"));
    }

    public static void truncateTable(String tableName) {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("TRUNCATE TABLE " + tableName.trim() + " RESTART IDENTITY CASCADE;");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void dropTable(String... tableNames) {
        for (String table : tableNames) {
            try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.executeUpdate("DROP TABLE " + table.trim() + ";");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void createTable(String... tables) {
        for (String path : tables) {
            try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.executeUpdate(Resource.get("sql/" + path).trim());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void insertDemoData(ILicenceRepository licenceRepository) {
        licenceRepository.save(new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now()));
        licenceRepository.save(new Licence("2-2", "typ2", 2, 2, 2, LocalDateTime.now()));
        licenceRepository.save(new Licence("3-3", "typ3", 3, 3, 3, LocalDateTime.now()));
    }

    public static void insertAdmin(IUserRepository userRepository) {
        userRepository.createUser("admin", "admin", "admin");
    }

    public static List<Licence> saveDemoLicencesToDbAndReturnAsList(ILicenceRepository licenceRepository) {
        List<Licence> licences = new ArrayList<>();

        Licence licence = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());
        licenceRepository.save(licence);
        licence.setId(1);
        licences.add(licence);

        licence = new Licence("1-2", "typ2", 2, 2, 2, LocalDateTime.now());
        licenceRepository.save(licence);
        licence.setId(2);
        licences.add(licence);

        licence = new Licence("1-3", "typ3", 3, 3, 3, LocalDateTime.now());
        licenceRepository.save(licence);
        licence.setId(3);
        licences.add(licence);

        return licences;
    }
}
