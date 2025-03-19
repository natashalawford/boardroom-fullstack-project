package ca.mcgill.ecse321.boardroom.dtos.creation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class BoardGameCreationDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Number of players must be at least 1.")
    private int playersNeeded;

    @NotNull(message = "A photo of the board game is required")
    private int picture;

    private BoardGameCreationDto() {}

    public BoardGameCreationDto(String title, String description,
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
