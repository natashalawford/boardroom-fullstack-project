package ca.mcgill.ecse321.eventregistration.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;


@Entity
public class Event {

    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String description;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private int maxParticipants;

    protected Event() {

    }

    public Event(int id, String title, String description,
                 ZonedDateTime startDateTime, ZonedDateTime endDateTime,
                 int maxParticipants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.maxParticipants = maxParticipants;
    }
}
