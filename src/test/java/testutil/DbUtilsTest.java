package testutil;

import com.roqqio.tselicence.core.DbSetup;
import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import com.roqqio.tselicence.core.entities.User;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceRepository;
import com.roqqio.tselicence.core.interfaces.repositories.IUserRepository;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

public class DbUtilsTest {
    public static final String LOGIN = "admin";
    public static final String PASSWORD = "admin";

    public static void drop(DbSetup dbSetup) {
        dbSetup.dropTable("licences", "request_logs", "users", "licences_detail");
    }

    public static void create(DbSetup dbSetup) {
        dbSetup.createTable("request_logs_table.sql", "licences_table.sql", "users_table.sql", "licences_detail_table.sql");
    }

    public static User getValidUser(IUserRepository userRepository) {
        User user = userRepository.get(LOGIN).get();
        user.setPassword(PASSWORD);
        return user;
    }

    public static void insertDefaultUser(Environment env, IUserRepository userRepository) {
        userRepository.createUser(env.getProperty("custom.username"), env.getProperty("custom.password"), "admin");
    }

    public static List<Licence> saveDemoLicencesToDbAndReturnAsList(ILicenceRepository licenceRepository) {
        List<Licence> licences = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Licence licence = new Licence();
            licence.setLicenceNumber(i + "-" + i);
            licence.setTseType("typ" + i);
            licence.setId(i);
            licences.add(licence);
        }
        licenceRepository.save(licences);
        return licences;
    }

    public static List<LicenceDetail> saveDemoLicencesDetailToDbAndReturnAsList(ILicenceDetailRepository licenceDetailRepository) {
        List<LicenceDetail> licencesDetail = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            LicenceDetail licenceDetail = new LicenceDetail();

            licenceDetail.setTillExternalId(String.valueOf(i));
            licenceDetail.setBranchNumber(String.valueOf(i));
            licenceDetail.setLicenceId(1);

            licencesDetail.add(licenceDetail);
        }
        return licenceDetailRepository.save(licencesDetail);
    }
}
