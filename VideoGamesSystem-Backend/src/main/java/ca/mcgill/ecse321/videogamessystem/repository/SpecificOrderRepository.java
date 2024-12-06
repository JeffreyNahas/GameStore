package ca.mcgill.ecse321.videogamessystem.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;

public interface SpecificOrderRepository extends CrudRepository<SpecificOrder, Integer>{
    
    SpecificOrder findOrderByNumber(int number);


    List<SpecificOrder> findOrderByCustomer(Customer customer);
}