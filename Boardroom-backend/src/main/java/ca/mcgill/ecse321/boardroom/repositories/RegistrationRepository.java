package ca.mcgill.ecse321.boardroom.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.Registration;
import ca.mcgill.ecse321.boardroom.model.Event;


public interface RegistrationRepository extends CrudRepository<Registration,
        Registration.Key> {
    public Registration findRegistrationByKey(Registration.Key key);
    public long countByKeyEvent(Event event); 
    public boolean existsByKeyPersonAndKeyEvent(Person person, Event event); 
    public List<Registration> findByKeyPerson(Person person);

}
