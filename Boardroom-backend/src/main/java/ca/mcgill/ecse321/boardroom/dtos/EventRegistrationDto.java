package ca.mcgill.ecse321.boardroom.dtos;

import java.time.LocalDateTime;

public class EventRegistrationDto {
    private int personId;
    private int eventId;
    private LocalDateTime registrationDate;

    public EventRegistrationDto() {
    }

    public EventRegistrationDto(int personId, int eventId, LocalDateTime registrationDate) {
        this.personId = personId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
