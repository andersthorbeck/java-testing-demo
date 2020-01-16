CREATE TABLE temperature (
  datetime TIMESTAMP UNIQUE NOT NULL,
  celsius REAL NOT NULL CHECK (celsius >= -273.15)
);
