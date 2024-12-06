package ca.mcgill.ecse321.videogamessystem.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;
import ca.mcgill.ecse321.videogamessystem.model.SpecificGame;

public interface SpecificGameRepository extends CrudRepository<SpecificGame, Integer>{
    
    SpecificGame findSpecificGameBySerialNumber(int serialNumber);

    List<SpecificGame> findSpecificGameByAvailability(boolean availability);

    List<SpecificGame> findSpecificGameByGame(Game game);

    List<SpecificGame> findSpecificGameBySpecificOrder(SpecificOrder specificOrder);
}
