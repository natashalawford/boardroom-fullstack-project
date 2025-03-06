package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;

public class SpecificBoardGameUpdateDto {
    private int id;
    private String description;
    private int picture;
    private GameStatus status;
    private String boardGameTitle;
    private int ownerId;

    private SpecificBoardGameUpdateDto() {}

    public SpecificBoardGameUpdateDto(int id, String description, int picture, GameStatus status, String boardGameTitle,
            int ownerId) {
        this.id = id;
        this.description = description;
        this.picture = picture;
        this.status = status;
        this.boardGameTitle = boardGameTitle;
        this.ownerId = ownerId;
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
