package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Location;


public interface LocationRepository extends CrudRepository<Location, Integer> {
    public Person findLocationById(int id);
}
