package ca.mcgill.ecse321.videogamessystem.dto.GameDto;

import ca.mcgill.ecse321.videogamessystem.model.Game.Category;
import ca.mcgill.ecse321.videogamessystem.model.Game.ConsoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GameRequestDto {

    @NotBlank(message = "Description cannot be blank.")
    private String description;

    @Positive(message = "Price must be a positive number.")
    @NotNull(message= "Price must be specified.")
    private int price;

    @NotBlank(message = "Title cannot be blank.")
    private String title;

    @NotNull(message = "Category must be specified.")
    private Category category;

    @NotNull(message = "Console type must be specified.")
    private ConsoleType consoleType;

    @Positive(message = "Stock quantity must be a positive number.")
    @NotNull(message = "Stock quantity must be specified.")
    private Integer stockQuantity;



    //maybe add fo

    public GameRequestDto(){}

    public GameRequestDto(String description, int price, String title, Category category, ConsoleType consoleType) {
        this.description = description;
        this.price = price;
        this.title = title;
        this.category = category;
        this.consoleType = consoleType;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStockQuantity(int stockQuantity){
        this.stockQuantity= stockQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public int getStockQuantity(){
        return stockQuantity;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ConsoleType getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(ConsoleType consoleType) {
        this.consoleType = consoleType;
    }
}
