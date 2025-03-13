package ca.mcgill.ecse321.boardroom.dtos;


public class PersonRequestDto {
    private String name;
    private String email;
    private boolean owner;

    private PersonRequestDto() {
    }

    public PersonRequestDto(String name, String email, boolean owner) {
        this.name = name;
        this.email = email;
        this.owner = owner;
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
