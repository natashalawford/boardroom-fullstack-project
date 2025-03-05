package ca.mcgill.ecse321.boardroom.dtos;

public class PersonCreationDto {
    private String name;
    private String email;
    private String password;
    private boolean owner;

    public PersonCreationDto(String name, String email, String password, boolean owner) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOwner() {
        return owner;
    }
}
