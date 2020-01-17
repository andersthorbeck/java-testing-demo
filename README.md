# Demo of testing in Java
This repository demonstrates various techniques for testing the different layers of Java applications.
The concepts are transferrable to other languages, but most of the demonstrated libraries and frameworks are exclusive to the JVM.

Unless otherwise specified, the relevant demo code is in the `climate` application.

## Libraries

### JUnit
The core testing framework for Java, which all the other libraries fit into.

### Hamcrest
A matching library, for checking whether values meet expectations.

See `TemperatureConverterTest` and `HamcrestDemonstration`.

### Mockito
A mocking library, for substituting real dependencies with mock dependencies, and stubbing the behaviour of these dependencies.
This is useful when you want to test only one class, when that class has dependencies to other classes.

See `TemperatureServiceTest`.

### Flyway
Framework for versioning the database structure.
Not strictly a testing-only framework, but invaluable when testing database access code.

See the `db/migration` resource folder, and the tests under the `no.knowit.kds2020.climate.db` package.

### H2
An example of an embedded in-memory database.
Useful for testing database access code, without depending on an external database.
Very fast, but is likely to differ somewhat in behaviour from your production database.

See `TemperatureRepositoryTestWithInMemoryDb`.

### Testcontainers
A library for running an ephemeral docker container with a given docker image as part of a Java test.
Useful for booting local, ephemeral, fully independent databases for testing your database access code.
Useful also for other external applications your application depends on, e.g. a message queue.


Slow, but allows you to run tests against exactly the same database as used in production, for more accurate test coverage.

See `TemperatureRepositoryTestWithContainerizedDb` and `TemperatureControllerEndToEndTest`.

### MockMvc
A part of the Spring Framework. Useful for testing the web layer of a Spring application. Actual HTTP networking is mocked.

See `TemperatureControllerMockMvcTest`. Contrast with `TemperatureControllerWebTest` (with a real server and real HTTP requests).

### Pact Framework
A framework for managing and running Consumer Driven Contracts.
A consumer defines a contract (Pact) against a provider API, stating what it expects from that API.
This pact is published, and the provider picks it up, and runs its code against the pact.
If the expectations fail, the test fails.

Useful for ensuring an API provider cannot inadvertently break behaviour that a consumer of that API depends on.
Gives the consumer a veto on changes to the provider, so it is best used when both consumer and provider are controlled by the same organization/people.

See `TemperatureApiPactConsumerTest` in the `grapher` application, and `PactProviderTest` in the `climate` application.

