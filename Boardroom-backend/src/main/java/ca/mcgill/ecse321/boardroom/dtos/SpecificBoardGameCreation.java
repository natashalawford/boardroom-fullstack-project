package ca.mcgill.ecse321.boardroom.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class SpecificBoardGameCreation {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Number of players must be at least 1.")
    private int playersNeeded;

    @NotBlank(message = "A photo of the board game is required")
    private int picture;

    @NotNull(message = "A board game is required")
    private int specificBoardGameId;

    @NotNull(message = "A board game owner is required")
    private int personId;

    public SpecificBoardGameCreation(String title, String description,
                            int playersNeeded, int picture, int specificBoardGameId, int personId) {
        this.title = title;
        this.description = description;
        this.playersNeeded = playersNeeded;
        this.picture = picture;
        this.specificBoardGameId = specificBoardGameId;
        this.personId = personId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() { 
        return description; 
    }

    public int getPlayersNeeded() {
        return playersNeeded;
    }

    public int getPicture() {
        return picture;
    }

    public int getSpecificBoardGameId() {
        return specificBoardGameId;
    }

    public int getPersonId() {
        return personId;
    }
}
