package no.knowit.kds2020.climate.db;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class TemperatureRepository {

  private final DataSource dataSource = createDataSource();

  private static DataSource createDataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
    dataSource.setUser("postgres");
    dataSource.setPassword("password");
    return dataSource;
  }

}
