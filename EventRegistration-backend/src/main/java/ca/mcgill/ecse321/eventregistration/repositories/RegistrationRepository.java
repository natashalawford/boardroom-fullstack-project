package ca.mcgill.ecse321.eventregistration.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Registration;


public interface RegistrationRepository extends CrudRepository<Registration,
        Registration.Key> {
    public Registration findRegistrationByKey(Registration.Key key);
}
