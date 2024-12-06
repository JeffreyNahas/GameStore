package ca.mcgill.ecse321.videogamessystem.dto.StaffDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StaffRequestDto {

    @NotBlank(message = "Username cannot be blank.")
    private String userName;

    @NotBlank(message = "Email cannot be blank.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    private String password;

    @NotNull(message = "Admin status must be specified.")
    private Boolean admin;
    

    public StaffRequestDto(String userName, String email, String password, Boolean admin) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    

    public Boolean getadmin(){
        return admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    
}
