package ca.mcgill.ecse321.videogamessystem.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.videogamessystem.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
    Customer findCustomerById(Long id);

    Customer findCustomerByUserName(String userName);

    Customer findCustomerByEmail(String email);
}