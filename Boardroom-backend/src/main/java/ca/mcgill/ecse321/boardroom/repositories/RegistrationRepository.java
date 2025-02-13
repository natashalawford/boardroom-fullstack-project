package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Registration;


public interface RegistrationRepository extends CrudRepository<Registration,
        Registration.Key> {
    public Registration findRegistrationByKey(Registration.Key key);
}
