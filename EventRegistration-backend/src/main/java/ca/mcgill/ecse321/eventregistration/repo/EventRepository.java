package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Event;


public interface EventRepository extends CrudRepository<Event, Integer> {
    public Person findEventById(int id);
}
