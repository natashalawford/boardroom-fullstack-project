package ca.mcgill.ecse321.boardroom.dtos;

public class EventRegistrationDto {
    private int personId;
    private int eventId;


    public EventRegistrationDto() {
    }

    public EventRegistrationDto(int personId, int eventId) {
        this.personId = personId;
        this.eventId = eventId;
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

}
