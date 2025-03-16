package ca.mcgill.ecse321.boardroom.dtos;

import ca.mcgill.ecse321.boardroom.model.Person;

public class PersonRequestDto {
    private String name;
    private String email;
    private boolean owner;

    private PersonRequestDto() {
    }

    public PersonRequestDto(Person person) {
        this.name = person.getName();
        this.email = person.getEmail();
        this.owner = person.isOwner();
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
