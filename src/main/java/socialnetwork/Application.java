package socialnetwork;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.neo4j.cypher.internal.compiler.v2_0.functions.Str;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.util.CollectionUtils;
import socialnetwork.dao.PersonRepository;
import socialnetwork.model.Person;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("application.properties")
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "socialnetwork")
@EnableNeo4jRepositories(basePackages = "socialnetwork")
public class Application extends Neo4jConfiguration implements CommandLineRunner {

    private PersonRepository personRepository;
    private GraphDatabase graphDatabase;
    private Neo4jTemplate neo4jTemplate;
    private String namesFile;
    private double avgNumOfFriends = 20.0;
    private double numOfFriendsStdDev = 5.0;

    public Application() {
        setBasePackage("socialnetwork");
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("neo4j-friends.db");
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Autowired
    public void setGraphDatabase(GraphDatabase graphDatabase) {
        this.graphDatabase = graphDatabase;
    }

    @Autowired
    public void setNeo4jTemplate(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    }

    public void setNamesFile(String namesFile) {
        this.namesFile = namesFile;
    }

    public void setAvgNumOfFriends(double avgNumOfFriends) {
        this.avgNumOfFriends = avgNumOfFriends;
    }

    public void setNumOfFriendsStdDev(double numOfFriendsStdDev) {
        this.numOfFriendsStdDev = numOfFriendsStdDev;
    }

    public void run(String... args) throws Exception {
        if (args.length < 1) {
            System.err.println("Missing command-line arguments: COMMAND");
            System.err.println("COMMAND:");
            System.err.println("  SETUP : create database, sample data and functions");
            System.err.println("  RUN : run example client");

            System.exit(1);
        }

        if ("SETUP".equalsIgnoreCase(args[0])) {
            setupExample();

        } else if ("RUN".equalsIgnoreCase(args[0])) {
            runExample();
        }
    }

    private void setupExample() throws IOException {
        Transaction tx = graphDatabase.beginTx();
        try {
            // list of social network users
            List<Person> people = new ArrayList<Person>();

            // read the names file
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(getClass().getResourceAsStream("/" + namesFile),
                            Charset.forName("UTF-8")));

            String line;
            while ((line = r.readLine()) != null) {
                Person p = new Person(line);
                personRepository.save(p);
                people.add(p);
                System.out.println("Saved: " + p.getName());
            }

            NormalDistribution dist = new NormalDistribution(avgNumOfFriends, numOfFriendsStdDev);
            for (Person person : people) {
                // choose a random number of friends
                int numOfFriends = (int) dist.sample();

                for (int i = 0; i < numOfFriends; i++) {
                    // choose a random friend
                    Person other = null;
                    // avoid self-friending :)
                    while (other == null || other == person) {
                        int index = (int) (Math.random() * people.size());
                        other = people.get(index);
                    }
                    person.friendOf(other);
                }

                personRepository.save(person);

                System.out.println(person.getName() + " has now " + person.getFriends().size() + " friends");
            }

            tx.success();

        } finally {
            tx.close();
        }
    }

    private void runExample() {
        Transaction tx = graphDatabase.beginTx();
        try {
            Person p = personRepository.findOne(1L);

            StringBuilder sb = new StringBuilder();
            sb.append("START p = node({personId})");
            sb.append(" MATCH p -[:FRIEND * 2..2]- friend_of_friend");
            sb.append(" WHERE NOT(p) -[:FRIEND]- friend_of_friend");
            sb.append(" RETURN friend_of_friend, COUNT(*) as cnt");
            sb.append(" ORDER BY COUNT(*) DESC , friend_of_friend.name");

            Map<String, Object> params = new HashMap<>();
            params.put("personId", p.getId());

            Result<Map<String, Object>> result = neo4jTemplate.query(sb.toString(), params);

            System.out.println("\nSuggested friends for " + p.getName() + ":");
            for (Map<String, Object> row : result) {
                int count = ((Number) row.get("cnt")).intValue();
                Person other = neo4jTemplate.convert(row.get("friend_of_friend"), Person.class);
                System.out.println(other.getName() + ", common friends: " + count);
            }

            tx.success();

        } finally {
            tx.close();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && "SETUP".equalsIgnoreCase(args[0])) {
            // clean-up previous runs
            FileUtils.deleteRecursively(new File("neo4j-friends.db"));
        }
        SpringApplication.run(Application.class, args);
    }

}