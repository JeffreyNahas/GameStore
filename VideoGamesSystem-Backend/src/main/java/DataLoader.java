// package ca.mcgill.ecse321.videogamessystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.videogamessystem.model.*;
import ca.mcgill.ecse321.videogamessystem.repository.*;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SpecificGameRepository specificGameRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create games
        Game game1 = new Game("An epic fantasy RPG.", 49, "Fantasy Realm", Game.Category.Action, Game.ConsoleType.XBOX);
        Game game2 = new Game("A fast-paced racing game.", 29, "Speed Racer", Game.Category.Arcade, Game.ConsoleType.PC);
        Game game3 = new Game("A challenging puzzle game.", 19, "Puzzle Master", Game.Category.Puzzle, Game.ConsoleType.Switch);
        Game game4 = new Game("A family-friendly platformer.", 39, "Platform Hero", Game.Category.Party, Game.ConsoleType.PS4);
        Game game5 = new Game("A realistic sports simulation.", 59, "Ultimate Soccer", Game.Category.Sports, Game.ConsoleType.XBOX);

        gameRepository.saveAll(Arrays.asList(game1, game2, game3, game4, game5));

    //     // Create SpecificGame instances
    //     SpecificGame sg1 = new SpecificGame(true, 1001, game1);
    //     SpecificGame sg2 = new SpecificGame(true, 1002, game1);
    //     SpecificGame sg3 = new SpecificGame(false, 1003, game1);

    //     SpecificGame sg4 = new SpecificGame(true, 2001, game2);
    //     SpecificGame sg5 = new SpecificGame(true, 2002, game2);

    //     SpecificGame sg6 = new SpecificGame(true, 3001, game3);

    //     SpecificGame sg7 = new SpecificGame(false, 4001, game4);
    //     SpecificGame sg8 = new SpecificGame(true, 4002, game4);

    //     SpecificGame sg9 = new SpecificGame(true, 5001, game5);
    //     SpecificGame sg10 = new SpecificGame(true, 5002, game5);
    //     SpecificGame sg11 = new SpecificGame(true, 5003, game5);

    //     specificGameRepository.saveAll(Arrays.asList(sg1, sg2, sg3, sg4, sg5, sg6, sg7, sg8, sg9, sg10, sg11));
    }
}
