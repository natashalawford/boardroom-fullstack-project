package ca.mcgill.ecse321.boardroom.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.BorrowRequest;

public interface BorrowRequestRepository extends CrudRepository<BorrowRequest, Integer> {
    public BorrowRequest findBorrowRequestById(int id);

    public List<BorrowRequest> findByBoardGameIdAndStatusIn(String specificBoardGameTitle,
            List<String> acceptedStatuses);
}
