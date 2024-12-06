package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.StaffDto.StaffRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.StaffDto.StaffResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.Staff;
import ca.mcgill.ecse321.videogamessystem.service.StaffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class StaffController {

    @Autowired
    private StaffService staffService;

    /**
     * Creates a new staff member.
     *
     * @param staffRequestDto the DTO containing the new staff's information.
     * @return a StaffResponseDto with the created staff's information.
     */
    @PostMapping("/staff")
    public StaffResponseDto createStaff(@Valid @RequestBody StaffRequestDto staffRequestDto) {
        Staff staff = staffService.createStaff(
                staffRequestDto.getUserName(),
                staffRequestDto.getEmail(),
                staffRequestDto.getPassword(),
                staffRequestDto.getadmin()
        );
        return new StaffResponseDto(staff);
    }

    /**
     * Retrieves a staff member by their ID.
     *
     * @param id the ID of the staff member to retrieve.
     * @return a StaffResponseDto with the staff member's information.
     */
    @GetMapping("/staff/{id}")
    public StaffResponseDto getStaffById(@PathVariable Long id) {
        Staff staff = staffService.getStaffById(id);
        return new StaffResponseDto(staff);
    }

    /**
     * Retrieves a staff member by their username.
     *
     * @param userName the username of the staff member to retrieve.
     * @return a StaffResponseDto with the staff member's information.
     */
    @GetMapping("/staff/username/{userName}")
    public StaffResponseDto getStaffByUserName(@PathVariable String userName) {
        Staff staff = staffService.getStaffByUserName(userName);
        return new StaffResponseDto(staff);
    }

    /**
     * Retrieves a staff member by their email.
     *
     * @param email the email of the staff member to retrieve.
     * @return a StaffResponseDto with the staff member's information.
     */
    @GetMapping("/staff/email/{email}")
    public StaffResponseDto getStaffByEmail(@PathVariable String email) {
        Staff staff = staffService.getStaffByEmail(email);
        return new StaffResponseDto(staff);
    }

    /**
     * Retrieves all staff members with a specific admin status.
     *
     * @param isAdmin the admin status to filter by (true for admin, false for non-admin).
     * @return a list of StaffResponseDto with matching staff members.
     */
    @GetMapping("/staff/admin/{isAdmin}")
    public List<StaffResponseDto> getStaffByAdmin(@PathVariable boolean isAdmin) {
        List<Staff> staffList = staffService.getStaffByAdmin(isAdmin);
        return staffList.stream().map(StaffResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Deletes a staff member by their ID.
     *
     * @param id the ID of the staff member to delete.
     * @return a StaffResponseDto with the deleted staff member's information.
     */
    @DeleteMapping("/staff/{id}")
    public StaffResponseDto deleteStaff(@PathVariable Long id) {
        Staff deletedStaff = staffService.deleteStaff(id);
        return new StaffResponseDto(deletedStaff);
    }

    /**
     * Retrieves all staff members in the system.
     *
     * @return a list of StaffResponseDto containing all staff members' information.
     */
    @GetMapping("/staff")
    public List<StaffResponseDto> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return staffList.stream().map(StaffResponseDto::new).collect(Collectors.toList());
    }
}
