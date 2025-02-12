package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.BorrowRequest;


public class BorrowRequestRepo extends CrudRepository<BorrowRequest, Integer> {
    
}
