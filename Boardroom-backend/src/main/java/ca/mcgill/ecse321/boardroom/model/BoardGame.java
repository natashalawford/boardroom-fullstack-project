package ca.mcgill.ecse321.boardroom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BoardGame {
    @Id
    private String title;
    private String description;
    private int playersNeeded;
    private int picture;

    protected BoardGame() {}

    public BoardGame(String title, String description, int playersNeeded,
                     int picture) {
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