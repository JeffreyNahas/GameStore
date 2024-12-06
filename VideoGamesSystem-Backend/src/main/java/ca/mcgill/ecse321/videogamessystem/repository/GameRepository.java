package ca.mcgill.ecse321.videogamessystem.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.Game.Category;
import ca.mcgill.ecse321.videogamessystem.model.Game.ConsoleType;
import ca.mcgill.ecse321.videogamessystem.model.Promotion;
import ca.mcgill.ecse321.videogamessystem.model.Wishlist;

public interface GameRepository extends CrudRepository<Game, Long>{

    Game findGameById(Long id);

    // List<Game> findGameByprice(int price);

    List<Game> findGameByTitle(String title);

    List<Game> findGameByCategory(Category category);

    List<Game> findGameByConsoleType(ConsoleType consoleType);

    List<Game> findGameByPromotion(Promotion promotion);

    List<Game> findGameByWishlist(Wishlist wishlist);

}