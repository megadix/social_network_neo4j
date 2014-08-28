package socialnetwork.dao;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import socialnetwork.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByName(String name);

    @Query("MATCH (n:Person {id: {0}})-[:FRIEND*2..2]-(friend_of_friend)" +
            " WHERE NOT (n)-[:FRIEND]-(friend_of_friend)" +
            " RETURN friend_of_friend, COUNT(*)" +
            " ORDER BY COUNT(*) DESC , friend_of_friend.name")
    Iterable<Person> recommendFriends(Long personId);
}
