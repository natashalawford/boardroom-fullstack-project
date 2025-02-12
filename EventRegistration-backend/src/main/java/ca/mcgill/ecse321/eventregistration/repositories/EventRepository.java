package ca.mcgill.ecse321.eventregistration.repositories;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
    public Event findEventById(int id);
}
