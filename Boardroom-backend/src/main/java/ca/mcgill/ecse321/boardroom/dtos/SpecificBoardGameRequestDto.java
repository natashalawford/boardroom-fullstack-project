package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SpecificBoardGameRequestDto {
    @NotBlank(message = "")
    private String description;

    @NotNull(message = "")
    private int picture;

    @NotNull(message = "")
    private GameStatus status;

    private SpecificBoardGameRequestDto() {}

    public SpecificBoardGameRequestDto(String description, int picture, GameStatus status) {
        
        this.description = description;
        this.picture = picture;
        this.status = status;
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
}
