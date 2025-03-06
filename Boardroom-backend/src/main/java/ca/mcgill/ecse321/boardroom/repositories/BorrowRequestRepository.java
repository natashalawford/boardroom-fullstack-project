package ca.mcgill.ecse321.boardroom.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;

public interface BorrowRequestRepository extends CrudRepository<BorrowRequest, Integer> {
    public BorrowRequest findBorrowRequestById(int id);

    List<BorrowRequest> findBySpecificBoardGameAndStatus(SpecificBoardGame specificBoardGame, RequestStatus status);
}
