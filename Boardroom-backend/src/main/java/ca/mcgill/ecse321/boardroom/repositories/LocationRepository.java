package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Location;

public interface LocationRepository extends CrudRepository<Location, Integer> {
    public Location findLocationById(int id);
}
