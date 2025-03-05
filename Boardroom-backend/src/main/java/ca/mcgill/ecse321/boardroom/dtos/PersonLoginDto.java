package ca.mcgill.ecse321.boardroom.dtos;

public class PersonLoginDto {
    private String email;
    private String password;

    public PersonLoginDto() {}

    public PersonLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { 
        return email; }

    public String getPassword() { 
        return password; }

}
