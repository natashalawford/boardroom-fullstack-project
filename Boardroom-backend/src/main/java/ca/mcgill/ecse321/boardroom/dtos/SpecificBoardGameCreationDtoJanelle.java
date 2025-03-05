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

    public SpecificBoardGameCreationDtoJanelle(String description, int picture,
                                        String gameStatus, String boardGameTitle,
                                        int personId) {
        this.description = description;
        this.picture = picture;
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
