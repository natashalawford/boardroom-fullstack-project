package ca.mcgill.ecse321.boardroom.dtos;

import jakarta.validation.constraints.NotBlank;

public class PersonUpdatePasswordDto {
    @NotBlank(message="The old password cannot be blank")
    private String oldPassword;
    @NotBlank(message="The new password cannot be blank")
    private String newPassword;

    // Constructor
    public PersonUpdatePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Default constructor
    private PersonUpdatePasswordDto() {}

    // Getters and Setters
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}