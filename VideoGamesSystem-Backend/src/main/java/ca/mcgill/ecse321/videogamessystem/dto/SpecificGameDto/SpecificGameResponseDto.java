package ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto;

import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.model.SpecificGame;

public class SpecificGameResponseDto {

    private boolean availability;
    private int serialNumber;
    private Game game;
    private String title;
    private String description;
    private int price;
    private Game.Category category;
    private Game.ConsoleType consoleType;
    private Integer orderNumber; // Added field for order number
    // Constructor with all relevant attributes
    protected SpecificGameResponseDto() {
      
    }

    // Constructor that initializes fields from a SpecificGame entity
    public SpecificGameResponseDto(SpecificGame specificGame) {
        this.availability = specificGame.getAvailability();
        this.serialNumber = specificGame.getSerialNumber();
        
        // Initialize fields from the associated Game entity
        // Game game = specificGame.getGame();
        this.game = specificGame.getGame();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.price = game.getPrice();  
        this.category = game.getCategory();
        this.consoleType = game.getConsoleType();
                // Add order number if the specific game has an order
        if (specificGame.getSpecificOrder() != null) {
            this.orderNumber = specificGame.getSpecificOrder().getNumber();
        }
            
        
    }

    // Getters and Setters
    public boolean isAvailability() {
        return availability;
    }



    public int getSerialNumber() {
        return serialNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }



    // public Long getGameId() {
    //     return gameId;
    // }



    public String getTitle() {
        return title;
    }

    public Game getGame(){
        return this.game;
    }



    public String getDescription() {
        return description;
    }

 

    public int getPrice() {
        return price;
    }

  

    public Game.Category getCategory() {
        return category;
    }

   

    public Game.ConsoleType getConsoleType() {
        return consoleType;
    }

}