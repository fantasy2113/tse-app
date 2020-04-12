git@git.futura.lokal:jos/tselizenzpruefung.git
mvn clean install -Dmaven.test.skip=true
pkill -f target/tselizenzpruefung-1.0-SNAPSHOT.jar || true
mvn spring-boot:run