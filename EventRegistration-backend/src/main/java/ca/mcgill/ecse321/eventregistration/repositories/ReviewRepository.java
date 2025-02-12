package ca.mcgill.ecse321.eventregistration.repositories;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Review;


public interface ReviewRepository extends CrudRepository<Review, Integer> {
    public Review findReviewById(int id);
}
