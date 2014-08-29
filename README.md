# Social network example using Neo4J

This project is part of my Bachelor's Degree work:

*"Comparative Analysis of NoSQL Databases and Applications"*  
Università degli Studi di Milano-Bicocca

Relator: Andrea Maurino  
Co-relator: Blerina Spahiu

# Tutorial

## Prerequisites

- Java 1.7 or newer

## Build from source

### Download code

You can either:

- **download** directly a tar/zip of *master* branch from GitHub repository:  
https://github.com/megadix/social_network_neo4j/archive/master.zip
- **clone the repository**:  
git@github.com:megadix/social_network_neo4j.git

### Build

Open a command line terminal in the root directory and issue this command:

On Windows:

    gradlew build

On Linux/Unix:

    chmod u+x gradlew
    ./gradlew build

***gradlew*** is the [Gradle Wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html), a small script and library (under */gradle* sub-directory) that provides  the Gradle build system, so you don't have to install it on your machine.

    >gradlew build
    :compileJava
    :processResources
    :classes
    :jar
    :bootRepackage
    :assemble
    :compileTestJava UP-TO-DATE
    :processTestResources UP-TO-DATE
    :testClasses UP-TO-DATE
    :test UP-TO-DATE
    :check UP-TO-DATE
    :build

    BUILD SUCCESSFUL

The build process creates a */build* sub-directory containing all the artefacts.

## Setup test data

To create test data you must run the application in SETUP mode:

On Windows:

    java -jar build\libs\megadix-social-network-neo4j-1.0.0.jar SETUP

On Linux/Unix:

    java -jar build/libs/megadix-social-network-neo4j-1.0.0.jar SETUP

The output will be similar to this (cut for brevity):

      .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::        (v1.1.5.RELEASE)

    2014-08-29 13:45:18.529  INFO 7404 --- [           main] socialnetwork.Application                : Starting Application on dimitri-PC with PID 7404 (G:\progetti\dimitri\universita\stage e laurea\progetto\experiments\social_network_neo4j\build\libs\megadix-social-network-neo4j-1.0.0.jar started by dimitri in G:\progetti\dimitri\universita\stage e laurea\progetto\experiments\social_network_neo4j)
    2014-08-29 13:45:18.617  INFO 7404 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@5a4a55c9: startup date [Fri Aug 29 13:45:18 CEST 2014]; root of context hierarchy
    2014-08-29 13:45:19.403  WARN 7404 --- [           main] org.neo4j.kernel.EmbeddedGraphDatabase   : You are using an unsupported version of the Java runtime. Please use Oracle(R) Java(TM) Runtime Environment 7 or OpenJDK(TM) 7.
    2014-08-29 13:45:19.437  INFO 7404 --- [           main] o.n.k.InternalAbstractGraphDatabase      : No locking implementation specified, defaulting to 'community'
    2014-08-29 13:45:25.144  INFO 7404 --- [           main] o.s.t.jta.JtaTransactionManager          : Using JTA UserTransaction: org.neo4j.kernel.impl.transaction.UserTransactionImpl@6fdad795
    2014-08-29 13:45:25.144  INFO 7404 --- [           main] o.s.t.jta.JtaTransactionManager          : Using JTA TransactionManager: org.neo4j.kernel.impl.transaction.SpringTransactionManager@544e2133
    Saved: Abel Derrickson
    Saved: Abel Fogg
    Saved: Adrian Dorado
    Saved: Afton Mccabe
    Saved: Alfred Haugen
    Saved: Aliza Thai
    Saved: Amiee Geno
    Saved: Antoinette Ketterman
    Saved: Anton Phinney
	
	[...]
	
    Saved: Yoshiko Slape
    Saved: Zola Freeborn
    Saved: Zola Rieves
    Abel Derrickson has now 27 friends
    Abel Fogg has now 21 friends
    Adrian Dorado has now 22 friends
    Afton Mccabe has now 10 friends
    Alfred Haugen has now 27 friends
    Aliza Thai has now 30 friends
    
	[...]
	
    Zola Freeborn has now 27 friends
    Zola Rieves has now 40 friends
    2014-08-29 13:45:46.340  INFO 7404 --- [           main] socialnetwork.Application                : Started Application in 28.42 seconds (JVM running for 29.284)
    2014-08-29 13:45:46.342  INFO 7404 --- [       Thread-2] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@5a4a55c9: startup date [Fri Aug 29 13:45:18 CEST 2014]; root of context hierarchy

### Run example

To view and example data extraction, execute the application in RUN mode:

On Windows:

    java -jar build\libs\megadix-social-network-neo4j-1.0.0.jar RUN

On Linux/Unix:

    java -jar build/libs/megadix-social-network-neo4j-1.0.0.jar RUN

The application will load a list of potential friends of *Abel Fogg*:

      .   ____          _            __ _ _
     /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
     \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
      '  |____| .__|_| |_|_| |_\__, | / / / /
     =========|_|==============|___/=/_/_/_/
     :: Spring Boot ::        (v1.1.5.RELEASE)

    2014-08-29 13:48:18.294  INFO 5796 --- [           main] socialnetwork.Application                : Starting Application on dimitri-PC with PID 5796 (G:\progetti\dimitri\universita\stage e laurea\progetto\experiments\social_network_neo4j\build\libs\megadix-social-network-neo4j-1.0.0.jar started by dimitri in G:\progetti\dimitri\universita\stage e laurea\progetto\experiments\social_network_neo4j)
    2014-08-29 13:48:18.378  INFO 5796 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@3fa901ec: startup date [Fri Aug 29 13:48:18 CEST 2014]; root of context hierarchy
    2014-08-29 13:48:19.120  WARN 5796 --- [           main] org.neo4j.kernel.EmbeddedGraphDatabase   : You are using an unsupported version of the Java runtime. Please use Oracle(R) Java(TM) Runtime Environment 7 or OpenJDK(TM) 7.
    2014-08-29 13:48:19.159  INFO 5796 --- [           main] o.n.k.InternalAbstractGraphDatabase      : No locking implementation specified, defaulting to 'community'
    2014-08-29 13:48:24.531  INFO 5796 --- [           main] o.s.t.jta.JtaTransactionManager          : Using JTA UserTransaction: org.neo4j.kernel.impl.transaction.UserTransactionImpl@227ebb1b
    2014-08-29 13:48:24.532  INFO 5796 --- [           main] o.s.t.jta.JtaTransactionManager          : Using JTA TransactionManager: org.neo4j.kernel.impl.transaction.SpringTransactionManager@343a0194

    Suggested friends for Abel Fogg:
    Leonard Ownbey, common friends: 10
    Markita Bradbury, common friends: 10
    Sheron Sandridge, common friends: 10
    Yesenia Chenail, common friends: 10
    Dona Transue, common friends: 9
    Ervin Hunton, common friends: 9
    Glen Lambros, common friends: 9
    Janean Hammack, common friends: 9
    Mindi Bondy, common friends: 9
    Deanna Paxson, common friends: 8
    Eulah Oriol, common friends: 8
    Gala Pifer, common friends: 8
    
    [...]
