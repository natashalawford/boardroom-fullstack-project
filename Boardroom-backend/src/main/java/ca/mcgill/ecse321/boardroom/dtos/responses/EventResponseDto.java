package ca.mcgill.ecse321.boardroom.dtos.responses;

import ca.mcgill.ecse321.boardroom.model.Event;

import java.time.LocalDateTime;

public class EventResponseDto {
    private int id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int maxParticipants;
    private String location;
    private int hostId;
    private String boardGameName;

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
        this.hostId = event.getEventHost().getId();
        this.boardGameName = event.getBoardGame().getTitle();
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

    public String getLocation() {
        return location;
    }

    public int getHostId() {
        return hostId;
    }

    public String getBoardGameName() {
        return boardGameName;
    }
}
