package no.knowit.kds2020.climate.db.script;

import org.flywaydb.core.Flyway;
import org.junit.Ignore;
import org.junit.Test;

public class ManuallySeedDatabaseScript {

  /** These values must match those in application.properties. */
  private static final String DATA_SOURCE_URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String DATA_SOURCE_USERNAME = "postgres";
  private static final String DATA_SOURCE_PASSWORD = "password";

  @Test
  @Ignore("this is a script, not a test, and should not normally be run")
  public void populateDatabase() {
    Flyway flyway = Flyway.configure()
        .dataSource(DATA_SOURCE_URL, DATA_SOURCE_USERNAME, DATA_SOURCE_PASSWORD)
        .locations("db/migration", "graphableSeed")
        .load();
    flyway.migrate();
  }

  /** To retest this, you must drop the existing tables to reset the flyway state.
   *    DROP TABLE temperature;
   *    DROP TABLE flyway_schema_history;
   */

}
