package ca.mcgill.ecse321.videogamessystem.dto.CustomerDto;
import ca.mcgill.ecse321.videogamessystem.model.Customer;



public class CustomerResponseDto {

    private Long id;
    private String userName;
    private String email;
    private int phoneNumber;
    private String address;



    public CustomerResponseDto() {
        super();
    }

    public CustomerResponseDto(Customer model) {

        this.id = model.getId();
        this.userName = model.getUserName();
        this.email = model.getEmail();
        this.phoneNumber = model.getPhoneNumber();
        this.address = model.getAddress();
        
    }

    // Getters
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id= id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber= phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address){
        this.address= address;
    }

}