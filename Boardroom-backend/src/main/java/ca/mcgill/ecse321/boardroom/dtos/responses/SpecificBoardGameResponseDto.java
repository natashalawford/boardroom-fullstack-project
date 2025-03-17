package ca.mcgill.ecse321.boardroom.dtos.responses;

import ca.mcgill.ecse321.boardroom.model.SpecificBoardGame;
import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;

public class SpecificBoardGameResponseDto {
    private int id;
    private String description;
    private int picture;
    private GameStatus status;
    private String boardGameTitle;

    //might not need this since the request is coming from an owner, so id is already knwon
    int ownerId;

    private SpecificBoardGameResponseDto() {}

    public SpecificBoardGameResponseDto(SpecificBoardGame specificBoardGame) {
        this.id = specificBoardGame.getId();
        this.description = specificBoardGame.getDescription();
        this.picture = specificBoardGame.getPicture();
        this.status = specificBoardGame.getStatus();
        this.boardGameTitle = specificBoardGame.getBoardGame().getTitle();
        this.ownerId = specificBoardGame.getOwner().getId();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getPicture() {
        return picture;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getBoardGameTitle() {
        return boardGameTitle;
    }

    public int getOwnerId() {
        return ownerId;
    } 
}
