package socialnetwork;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import socialnetwork.dao.PersonRepository;
import socialnetwork.model.Person;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "socialnetwork")
@EnableNeo4jRepositories(basePackages = "socialnetwork")
public class Application extends Neo4jConfiguration implements CommandLineRunner {

    private String embeddedDatabaseName = "socialnetwork.db";
    private PersonRepository personRepository;
    private GraphDatabase graphDatabase;
    private double avgNumOfFriends = 20.0;
    private double numOfFriendsStdDev = 5.0;

    public Application() {
        setBasePackage("socialnetwork");
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(embeddedDatabaseName);
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Autowired
    public void setGraphDatabase(GraphDatabase graphDatabase) {
        this.graphDatabase = graphDatabase;
    }

    public void run(String... args) throws Exception {
        Transaction tx = graphDatabase.beginTx();
        try {
            // run example
            buildSampleSocialNetwork();

            tx.success();
        } finally {
            tx.close();
        }
    }

    private void buildSampleSocialNetwork() throws Exception {
        // list of social network users
        List<Person> people = new ArrayList<Person>();

        // read the names file
        BufferedReader r = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/names.txt"),
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
                    int index = (int) (Math.random() * 100.0);
                    other = people.get(index);
                }
                person.friendOf(other);
            }

            personRepository.save(person);

            System.out.println(person.getName() + " has now " + person.getFriends().size() + " friends");
        }

        System.out.println("TODO queries");


    }

    public static void main(String[] args) throws Exception {
        // clean-up previous runs
        FileUtils.deleteRecursively(new File("accessingdataneo4j.db"));
        SpringApplication.run(Application.class, args);
    }

}