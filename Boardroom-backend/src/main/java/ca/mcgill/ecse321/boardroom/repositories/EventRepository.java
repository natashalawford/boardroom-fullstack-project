package ca.mcgill.ecse321.boardroom.repositories;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
    public Event findEventById(int id);
}
