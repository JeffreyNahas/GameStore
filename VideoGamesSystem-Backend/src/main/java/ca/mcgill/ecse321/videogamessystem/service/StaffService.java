package ca.mcgill.ecse321.videogamessystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Staff;
import ca.mcgill.ecse321.videogamessystem.repository.StaffRepository;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

      /**
     * @param userName
     * @param email
     * @param password
     * @param isAdmin
     * @return
     */
    @Transactional
    public Staff createStaff(String userName, String email, String password, boolean isAdmin) {
        if (isAdmin == true){
            List<Staff> boss = this.getStaffByAdmin(true);
            if (boss.size() != 0){
                throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"cannot have 2 owners");
            }
        }
        // Validation logic can be added here (e.g., check if email or username is already taken)
        //username check

        //email check
        if (email == null || email.trim().length() == 0){
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"no empty email");
        }
        if (!Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"invalid email");
        }
        if (staffRepository.findStaffByEmail(email) != null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Email already exists");
        }
        if (staffRepository.findStaffByUserName(userName) != null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"UserName already exists");
        }
        //password check
        if (password == null || password.trim().length() < 4){
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"password must be more than 4 characters");
        }

        Staff staff = new Staff(userName, email, password, isAdmin);
        return staffRepository.save(staff);
    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public Staff getStaffById(Long id) {
        Staff staff = staffRepository.findStaffById(id);
        if (staff == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Staff not found.");
        }
        return staff;
    }

    /**
     * @param userName
     * @return
     */
    @Transactional
    public Staff getStaffByUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Username cannot be empty.");
        }
        return staffRepository.findStaffByUserName(userName);
    }

    /**
     * @param email
     * @return
     */
    @Transactional
    public Staff getStaffByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Email cannot be empty.");
        }
        return staffRepository.findStaffByEmail(email);
    }

    /**
     * @param isAdmin
     * @return
     */
    @Transactional
    public List<Staff> getStaffByAdmin(boolean isAdmin) {
        return staffRepository.findStaffByAdmin(isAdmin);
    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public Staff deleteStaff(Long id) {
        Staff staff = staffRepository.findStaffById(id);
        if (staff == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Staff not found.");
        }
        if (staff.getStaffType() == true){
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"cannot delete owner");
        }

        staffRepository.delete(staff);
        return staff;
    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public Staff setOwner(Long id) {
        Staff staff = staffRepository.findStaffById(id);
        if (staff == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Staff not found.");
        }
        
        List<Staff> currentOwnerList = staffRepository.findStaffByAdmin(true);
        //if no owner yet
        if (currentOwnerList.size() == 0){
            staff.setStaffType(true);
            return staff;
        }
        Staff boss = currentOwnerList.get(0);
        
        //the boss steps down
        boss.setStaffType(false);
        staff.setStaffType(true);
        staffRepository.save(boss);
        return staffRepository.save(staff);
    }

    /**
     * @return
     */
    @Transactional
    public List<Staff> getAllStaff() {
        return toList(staffRepository.findAll());
    }

    /**
     * Converts an {@code Iterable} to a {@code List}.
     * @param iterable the {@code Iterable} to convert
     * @param <T>      the type of elements in the iterable
     * @return a {@code List} containing the elements of the {@code Iterable}
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}

