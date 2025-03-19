package ca.mcgill.ecse321.boardroom.dtos;

public class PersonUpdatePasswordDto {
    private String oldPassword;
    private String newPassword;

    // Constructor
    public PersonUpdatePasswordDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // Default constructor
    public PersonUpdatePasswordDto() {}

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