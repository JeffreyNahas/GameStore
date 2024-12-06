package ca.mcgill.ecse321.videogamessystem.dto.CustomerDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;



public class CustomerRequestDto {

// add the null blamk and everuthing

    @NotBlank(message = "Customer username cannot be empty")
    private String userName;

    @NotBlank(message = "Customer email cannot be empty")
    private String email;

    @NotBlank(message = "Customer password cannot be empty")
    private String password;

    @NotNull(message = "Game ID cannot be null")
    @Positive(message = "Game ID must be positive") 
    private int phoneNumber;

    @NotBlank(message = "Customer address cannot be empty")
    private String address;



    public CustomerRequestDto(){
        super();
    }


    public CustomerRequestDto(String userName, String email, String password, int phoneNumber, String address) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address= address;
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
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

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;

    }
// pas besoin de get et set wishlist apparemment :

}
