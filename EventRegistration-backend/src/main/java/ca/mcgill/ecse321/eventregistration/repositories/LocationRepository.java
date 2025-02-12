package ca.mcgill.ecse321.eventregistration.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Location;

public interface LocationRepository extends CrudRepository<Location, Integer> {
    public Location findLocationById(int id);
}
