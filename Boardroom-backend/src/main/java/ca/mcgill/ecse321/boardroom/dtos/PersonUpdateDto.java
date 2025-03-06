package ca.mcgill.ecse321.boardroom.dtos;

public class PersonUpdateDto {
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean owner;

    public PersonUpdateDto(int id, String name, String email, String password, boolean owner) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.owner = owner;
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

    public String getPassword() {
        return password;
    }

    public boolean isOwner() {
        return owner;
    }
}
