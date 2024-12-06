package ca.mcgill.ecse321.videogamessystem.dto.StaffDto;

import ca.mcgill.ecse321.videogamessystem.model.Staff;

public class StaffResponseDto {
    private Long id;
    private String userName;
    private String email;
    private Boolean admin;

    public StaffResponseDto(){}

    public StaffResponseDto(Long id,String userName, String email, Boolean admin){
        this.id =id;
        this.userName = userName;
        this.email = email;
        this.admin = admin;
    }

    public StaffResponseDto(Staff staff) {
        this.id = staff.getId();
        this.userName = staff.getUserName();
        this.email = staff.getEmail();
        this.admin = staff.getStaffType();
    }
    //figure out association

    // Getters 
    public Long getId() {
        return id;
    }


    public String getUserName() {
        return userName;
    }


    public String getEmail() {
        return email;
    }

  

    public Boolean getAdmin() {
        return admin;
    }

   
}
