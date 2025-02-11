package ca.mcgill.ecse321.eventregistration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import ca.mcgill.ecse321.eventregistration.model.enums.GameStatus;
import jakarta.persistence.ManyToOne;

@Entity
public class SpecificBoardGame {
    @Id
    @GeneratedValue
    private int id;
    private String description;
    private int picture;
    private GameStatus status;

    @ManyToOne
    private BoardGame boardGame;

    protected SpecificBoardGame() {}

    public SpecificBoardGame(int picture, String description,
                             GameStatus status, BoardGame boardGame) {
        this.picture = picture;
        this.description = description;
        this.status = status;
        this.boardGame = boardGame;
    }

    public int getId() {
        return id;
    }

    public int getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public GameStatus getStatus() {
        return status;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }
}