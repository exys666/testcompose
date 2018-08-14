# Test Compose

[JUnit5](https://junit.org/junit5/) extension for managing docker-compose files during test execution

## Requirements
- Java 8
- [Docker](https://www.docker.com/)
- [docker-compose](https://docs.docker.com/compose/)
- [JUnit5](https://junit.org/junit5/)


## Usage

###  Maven dependency

Maven central, [see](https://search.maven.org/search?q=g:com.testcompose%20AND%20a:junit-extension&core=gav)
```xml
<pom>
    
    <dependencies>
        <dependency>
            <groupId>com.testcompose</groupId>
            <artifactId>junit-extension</artifactId>
            <version>0.3.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</pom>
```

or JitPack release, [read more](https://jitpack.io/#exys666/testcompose/0.2.0)
```xml
<pom>

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    
    <dependencies>
        <dependency>
            <groupId>com.github.exys666</groupId>
            <artifactId>testcompose</artifactId>
            <version>0.2.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</pom>
```

Maven Central repository release coming soon!

### Simple usage

`src/test/resources/docker-compose-yml`
```yaml
version: '2.1'
services:
  postgres:
    image: 'postgres:9.6'
    environment:
      POSTGRES_DB: 'db'
      POSTGRES_USER: 'user'
      POSTGRES_PASSWORD: 'password'
    ports:
      - '5432'

```

`src/test/java/com/mydomain/SomeIntegrationTest.java`
```java
package com.mydomain;

import com.testcompose.TestComposeExtension;
import com.testcompose.annotation.Await;
import com.testcompose.annotation.Compose;
import com.testcompose.annotation.Port;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(TestComposeExtension.class)
@Compose(
        id = "my-id",
        exportPorts = @Port(container = "postgres", port = 5432, as = "postgres.port"),
        waitFor = @Await(container = "postgres", message = "database system is ready to accept connections")
)
class SomeIntegrationTest {
    
    @Test
    void someTest() {
        String port = System.getProperty("postgres.port");
        ...
    }    
}
```

### Attaching to running container composition

Extension can attach to running docker composition. 
In order to do that docker compose needs to be run with same `id` as in *@Compose* annotation

```sh
docker-compose \
    -f src/test/resources/docker-compose.yml \
    -p my-id up \
    --build
```

### Integrating with Spring

`src/test/java/com/mydomain/SpringIntegrationTest.java`
```java
package com.mydomain;

import com.testcompose.TestComposeExtension;
import com.testcompose.annotation.Await;
import com.testcompose.annotation.Compose;
import com.testcompose.annotation.Port;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(TestComposeExtension.class)
@ExtendWith(SpringExtension.class)
@Compose(
        id = "my-id",
        exportPorts = @Port(container = "postgres", port = 5432, as = "postgres.port"),
        waitFor = @Await(container = "postgres", message = "database system is ready to accept connections")
)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringIntegrationTest {
    
    @Test
    void springTest() {
        ...
    }    
}
```

`src/test/resources/application-test.properties`
```properties
db.url=postgres://user:password@0.0.0.0:${postgres.port}/db
```
