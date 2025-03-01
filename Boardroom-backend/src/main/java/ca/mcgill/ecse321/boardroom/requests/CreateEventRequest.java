package ca.mcgill.ecse321.boardroom.requests;

import ca.mcgill.ecse321.boardroom.model.BoardGame;
import ca.mcgill.ecse321.boardroom.model.Location;
import ca.mcgill.ecse321.boardroom.model.Person;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CreateEventRequest {
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int maxParticipants;
    private Location location;
    private Person eventHost;
    private BoardGame boardGame;

    public CreateEventRequest(String title, String description,
                 LocalDateTime startDateTime, LocalDateTime endDateTime,
                 int maxParticipants, Location location, Person eventHost, BoardGame boardGame) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.maxParticipants = maxParticipants;
        this.location = location;
        this.eventHost = eventHost;
        this.boardGame = boardGame;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() { return description; }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public Location getLocation() {
        return location;
    }

    public Person getEventHost() {
        return eventHost;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
