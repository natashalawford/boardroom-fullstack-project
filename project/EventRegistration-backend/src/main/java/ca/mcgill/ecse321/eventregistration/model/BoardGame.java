package ca.mcgill.ecse321.eventregistration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BoardGame {
    @Id
    private String title;
    private String description;
    private int playersNeeded;

    protected BoardGame() {}

    public BoardGame(String title, String description, int playersNeeded) {
        this.title = title;
        this.description = description;
        this.playersNeeded = playersNeeded;
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
}