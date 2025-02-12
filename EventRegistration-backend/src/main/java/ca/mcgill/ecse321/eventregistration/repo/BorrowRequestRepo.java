package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.BorrowRequest;


public interface BorrowRequestRepo extends CrudRepository<BorrowRequest, Integer> {
    
}
