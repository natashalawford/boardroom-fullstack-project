package ca.mcgill.ecse321.boardroom.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.mcgill.ecse321.boardroom.model.BorrowRequest;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.RequestStatus;

public interface BorrowRequestRepository extends CrudRepository<BorrowRequest, Integer> {
    public BorrowRequest findBorrowRequestById(int id);

    List<BorrowRequest> findBySpecificBoardGameAndStatus(SpecificBoardGame specificBoardGame, RequestStatus status);
    List<BorrowRequest> findByPersonAndStatus(Person person, RequestStatus status);
    List<BorrowRequest> findByStatus(RequestStatus status);
    void deleteAllBySpecificBoardGame(SpecificBoardGame game);

    @Query("SELECT br FROM BorrowRequest br WHERE br.specificBoardGame.owner.id = :ownerId AND br.status = :status")
    List<BorrowRequest> findByOwnerIdAndStatus(@Param("ownerId") int ownerId, @Param("status") RequestStatus status);

}
