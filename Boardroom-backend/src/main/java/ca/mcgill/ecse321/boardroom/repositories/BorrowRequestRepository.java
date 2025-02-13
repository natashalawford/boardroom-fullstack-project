package ca.mcgill.ecse321.boardroom.repositories;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.BorrowRequest;

public interface BorrowRequestRepository extends CrudRepository<BorrowRequest, Integer> {
    public BorrowRequest findBorrowRequestById(int id);
}
