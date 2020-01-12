package no.knowit.kds2020.climate.db;

import org.flywaydb.core.Flyway;
import org.junit.Test;

public class DatabaseTest {

  @Test
  public void test() {
    //$ docker run -u postgres -p 5432:5432 -e POSTGRES_PASSWORD=password postgres:9
    //$ psql -h localhost -U postgres

    Flyway flyway = Flyway.configure()
        .dataSource("jdbc:postgresql://localhost:5432/postgres", "postgres", "password")
        .load();

    flyway.migrate();

  }
}
