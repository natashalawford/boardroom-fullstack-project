package ca.mcgill.ecse321.boardroom.dtos.responses;

import ca.mcgill.ecse321.boardroom.model.Registration;

public class EventRegistrationResponseDto {
    private int personId;
    private int eventId;
    private String personName;
    private String eventName;
    private Registration.Key registrationId;

    public EventRegistrationResponseDto() {
    }

    public EventRegistrationResponseDto(Registration registration) {
        this.personId = registration.getKey().getPerson().getId();
        this.eventId = registration.getKey().getEvent().getId();
        this.personName = registration.getKey().getPerson().getName();
        this.eventName = registration.getKey().getEvent().getTitle();
        this.registrationId = registration.getKey();
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

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Registration.Key getRegistrationId() {
        return registrationId;
    }
    
}