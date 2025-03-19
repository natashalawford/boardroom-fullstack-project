package ca.mcgill.ecse321.boardroom.dtos;

import jakarta.validation.constraints.NotBlank;

public class PersonLoginDto {
    @NotBlank(message = "The email cannot be blank")
    private String email;
    @NotBlank(message = "The password cannot be blank")
    private String password;

    private PersonLoginDto() {
    }

    public PersonLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
