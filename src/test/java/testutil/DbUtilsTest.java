package testutil;

import de.jos.tselicence.core.entities.User;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.core.util.DbUtils;

public class DbUtilsTest {
    public static final String LOGIN = "admin";
    public static final String PASSWORD = "admin";

    public static void drop() {
        DbUtils.dropTable("licences", "request_logs", "users");
    }

    public static void create() {
        DbUtils.createTable("request_logs_table.sql", "licences_table.sql", "users_table.sql");
    }

    public static User getValidUser(IUserRepository userRepository) {
        User user = userRepository.get(LOGIN).get();
        user.setPassword(PASSWORD);
        return user;
    }
}
