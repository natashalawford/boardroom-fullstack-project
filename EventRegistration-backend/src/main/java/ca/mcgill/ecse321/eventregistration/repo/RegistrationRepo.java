package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Registration;


public interface RegistrationRepo extends CrudRepository<Registration, Integer> {
    
}
