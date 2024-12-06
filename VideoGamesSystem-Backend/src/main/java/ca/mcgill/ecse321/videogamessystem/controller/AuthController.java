package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerResponseDto;
import ca.mcgill.ecse321.videogamessystem.dto.StaffDto.StaffResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Staff;
import ca.mcgill.ecse321.videogamessystem.service.CustomerService;
import ca.mcgill.ecse321.videogamessystem.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StaffService staffService;

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> credentials) {
        String userName = credentials.get("userName");
        String password = credentials.get("password");
        String userType = credentials.get("userType"); // "customer" or "staff"

        if ("customer".equalsIgnoreCase(userType)) {
            Customer customer = customerService.getCustomerByUserName(userName);
            if (customer != null && customer.getPassword().equals(password)) {
                return new CustomerResponseDto(customer);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        } else if ("staff".equalsIgnoreCase(userType)) {
            Staff staff = staffService.getStaffByUserName(userName);
            if (staff != null && staff.getPassword().equals(password)) {
                return new StaffResponseDto(staff);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user type");
        }
    }
}
