package ca.mcgill.ecse321.boardroom.dtos.creation;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PersonCreationDto {
    @NotBlank(message = "The name must not be blank")
    private String name;

    @NotBlank(message = "The email must not be blank")
    private String email;

    @NotBlank(message = "The password must not be blank")
    private String password;

    @NotNull(message = "The owner field must not be null")
    private boolean owner;

    private PersonCreationDto() {}

    public PersonCreationDto(String name, String email, String password,
            boolean owner) {
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
