package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;

public class SpecificBoardGameCreationDto {
    private String description;
    private int picture;
    private GameStatus status;
    private String boardGameTitle;
    private int ownerId;

    private SpecificBoardGameCreationDto() {}

    public SpecificBoardGameCreationDto(String description, int picture, GameStatus status, String boardGameTitle,
            int ownerId) {
        this.description = description;
        this.picture = picture;
        this.status = status;
        this.boardGameTitle = boardGameTitle;
        this.ownerId = ownerId;
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

