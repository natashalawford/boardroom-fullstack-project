package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.enums.GameStatus;

public class SpecificBoardGameRequestDto {
    private String description;
    private int picture;
    private GameStatus status;

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
