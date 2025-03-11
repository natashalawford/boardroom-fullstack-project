package ca.mcgill.ecse321.boardroom.dtos.responses;

import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;

public class SpecificBoardGameResponseDto {
    private int picture;
    private String description;
    private GameStatus status;
    private String boardGameTitle;
    private int personId;
    private int id;

    @SuppressWarnings("unused")
    private SpecificBoardGameResponseDto() { }

    public SpecificBoardGameResponseDto(SpecificBoardGame specificBoardGame) {
        this.picture = specificBoardGame.getPicture();
        this.description = specificBoardGame.getDescription();
        this.status = specificBoardGame.getStatus();
        this.boardGameTitle = specificBoardGame.getBoardGame().getTitle();
        this.personId = specificBoardGame.getOwner().getId();
        this.id = specificBoardGame.getId();
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

    public String getBoardGameTitle() {
        return boardGameTitle;
    }

    public int getPersonId() {
        return personId;
    }

    public int getId() {
        return id;
    }
}
