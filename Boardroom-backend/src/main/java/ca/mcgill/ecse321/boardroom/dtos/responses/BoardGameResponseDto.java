package ca.mcgill.ecse321.boardroom.dtos.responses;

import ca.mcgill.ecse321.boardroom.model.BoardGame;

public class BoardGameResponseDto {
    private String title;
    private String description;
    private int playersNeeded;
    private int picture;

    @SuppressWarnings("unused")
    private BoardGameResponseDto() { }

    public BoardGameResponseDto(BoardGame boardGame) {
        this.title = boardGame.getTitle();
        this.description = boardGame.getDescription();
        this.playersNeeded = boardGame.getPlayersNeeded();
        this.picture = boardGame.getPicture();
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
