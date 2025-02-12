package ca.mcgill.ecse321.eventregistration.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.GameOwner;

public interface GameOwnerRepository extends CrudRepository<GameOwner, Integer> {
    public GameOwner findGameOwnerById(int id);
}
