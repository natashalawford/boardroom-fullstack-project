package ca.mcgill.ecse321.boardroom.repositories;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.Review;


public interface ReviewRepository extends CrudRepository<Review, Integer> {
    public Review findReviewById(int id);
}
