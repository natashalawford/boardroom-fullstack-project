package ca.mcgill.ecse321.boardroom.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SpecificBoardGameCreationDtoJanelle {
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "A photo of the board game is required")
    private int picture;

    @NotNull(message = "A board game status is required")
    private String gameStatus;

    @NotNull(message = "A board game title is required")
    private String boardGameTitle;

    @NotNull(message = "A board game owner is required")
    private int personId;

    public SpecificBoardGameCreationDtoJanelle(int picture, String description,
            String gameStatus, String boardGameTitle,
            int personId) {
        this.picture = picture;
        this.description = description;
        this.gameStatus = gameStatus;
        this.boardGameTitle = boardGameTitle;
        this.personId = personId;
    }

    public String getDescription() {
        return description;
    }

    public int getPicture() {
        return picture;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public String getBoardGameTitle() {
        return boardGameTitle;
    }

    public int getPersonId() {
        return personId;
    }
}
