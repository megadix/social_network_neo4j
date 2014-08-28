package socialnetwork.dao;

import org.springframework.data.repository.CrudRepository;
import socialnetwork.model.Person;

public interface PersonRepository extends CrudRepository<Person, String> {
    Person findByName(String name);
}
