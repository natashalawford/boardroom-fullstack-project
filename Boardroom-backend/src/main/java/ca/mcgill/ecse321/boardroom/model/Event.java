package ca.mcgill.ecse321.boardroom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Event {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int maxParticipants;

    @ManyToOne
    private Location location;
    @ManyToOne
    private Person eventHost;
    @ManyToOne
    private BoardGame boardGame;

    protected Event() {}

    public Event(String title, String description,
                 LocalDateTime startDateTime, LocalDateTime endDateTime,
                 int maxParticipants, Location location, Person eventHost) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.maxParticipants = maxParticipants;
        this.location = location;
        this.eventHost = eventHost;
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

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public Location getLocation() {
        return location;
    }

    public Person getEventHost() {
        return eventHost;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
