package main.java.ca.mcgill.ecse321.eventregistration.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Review;


public interface ReviewRepository extends CrudRepository<Review, Integer> {
    public Person findReviewById(int id);
}
