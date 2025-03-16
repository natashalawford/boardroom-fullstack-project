package ca.mcgill.ecse321.boardroom.dtos.responses;

import ca.mcgill.ecse321.boardroom.model.Person;

public class PersonResponseDto {
    int id;
    String name;
    String email;
    boolean owner;

    private PersonResponseDto() {}

    public PersonResponseDto(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.email = person.getEmail();
        this.owner = person.isOwner();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isOwner() {
        return owner;
    }
}
