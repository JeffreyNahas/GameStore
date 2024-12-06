package ca.mcgill.ecse321.videogamessystem.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import ca.mcgill.ecse321.videogamessystem.model.Staff;


public interface StaffRepository extends CrudRepository<Staff, Long>{
    Staff findStaffById(Long id);

    Staff findStaffByUserName(String userName);

    Staff findStaffByEmail(String email);

    List<Staff> findStaffByAdmin(boolean admin);
}