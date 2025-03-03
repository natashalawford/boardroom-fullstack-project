package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.Event;
import ca.mcgill.ecse321.boardroom.model.Location;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.model.BoardGame;

import java.time.LocalDateTime;

public class EventResponseDto {
    private int id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int maxParticipants;
    private Location location;
    private Person host;
    private BoardGame boardGame;

    @SuppressWarnings("unused")
    private EventResponseDto() { }

    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.startDateTime = event.getStartDateTime();
        this.endDateTime = event.getEndDateTime();
        this.maxParticipants = event.getMaxParticipants();
        this.location = event.getLocation();
        this.host = event.getEventHost();
        this.boardGame = event.getBoardGame();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public Location getLocation() {
        return location;
    }

    public Person getHost() {
        return host;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }
}
