package ca.mcgill.ecse321.videogamessystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.videogamessystem.model.Promotion;

@Repository
public interface PromotionRepository extends CrudRepository<Promotion, Long>{
    // find the promotion with id 
    Promotion findPromotionById(Long id);

}