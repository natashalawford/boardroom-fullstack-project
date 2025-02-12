package ca.mcgill.ecse321.eventregistration.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.BorrowRequest;

public interface BorrowRequestRepository extends CrudRepository<BorrowRequest, Integer> {
    public BorrowRequest findBorrowRequestById(int id);
}
