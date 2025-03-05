package ca.mcgill.ecse321.boardroom.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public class BoardGameCreationDtoJanelle {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Number of players must be at least 1.")
    private int playersNeeded;

    @NotBlank(message = "A photo of the board game is required")
    private int picture;

    public BoardGameCreationDtoJanelle(String title, String description,
                            int playersNeeded, int picture) {
        this.title = title;
        this.description = description;
        this.playersNeeded = playersNeeded;
        this.picture = picture;
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
}
