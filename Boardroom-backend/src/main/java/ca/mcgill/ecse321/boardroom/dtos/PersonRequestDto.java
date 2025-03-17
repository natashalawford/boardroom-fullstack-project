package ca.mcgill.ecse321.boardroom.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PersonRequestDto {
    @NotBlank(message = "The name must not be blank")
    private String name;

    @NotBlank(message = "The email must not be blank")   
    private String email;

    @NotNull(message = "The owner must not be null")
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
