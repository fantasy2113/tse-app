package de.jos.tselicence;

import de.jos.tselicence.config.Context;
import de.jos.tselicence.core.util.DbUtils;
import de.jos.tselicence.repositories.LicenceRepository;
import de.jos.tselicence.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        // dev only
        dbSetup();
    }

    private static void dbSetup() {
        DbUtils.dropTable("licences", "request_logs", "users");
        DbUtils.createTable("request_logs_table.sql", "licences_table.sql", "users_table.sql");
        DbUtils.insertDemoData(Context.getBean(LicenceRepository.class));
        DbUtils.insertAdmin(Context.getBean(UserRepository.class));
    }
}
