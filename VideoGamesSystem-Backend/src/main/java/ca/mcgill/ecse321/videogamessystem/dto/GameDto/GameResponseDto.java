package ca.mcgill.ecse321.videogamessystem.dto.GameDto;

import ca.mcgill.ecse321.videogamessystem.model.Game.Category;
import ca.mcgill.ecse321.videogamessystem.model.Game.ConsoleType;
import ca.mcgill.ecse321.videogamessystem.model.Game;
import ca.mcgill.ecse321.videogamessystem.dto.PromotionDto.PromotionResponseDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameResponseDto;
import java.util.List;
import java.util.stream.Collectors;

public class GameResponseDto {
    private Long id;
    private String description;
    private int price;
    private String title;
    private Category category;
    private ConsoleType consoleType;
    private PromotionResponseDto promotion;
    private List<SpecificGameResponseDto> specificGames;
/*  
    private PromotionResponseDto promotion;

    private WishlistResponseDto wishlist;
*/
    //maybe add associations

    public GameResponseDto() {
    }

    public GameResponseDto(Game game) {
        this.id = game.getId();
        this.description = game.getDescription();
        this.price = game.getPrice();
        this.title = game.getTitle();
        this.category = game.getCategory();
        this.consoleType = game.getConsoleType();
        this.promotion = game.getPromotion() != null ? new PromotionResponseDto(game.getPromotion()) : null;

        // this.promotion= PromotionResponseDto.convertToPromotionResponseDto(game.getPromotion());
        // this.wishlist= WishlistResponseDto.convertToWishlistResponseDto(game.getWishlist());

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public int getPrice() {
        return price;
    }


    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public List<SpecificGameResponseDto> getSpecificGames() {
        return specificGames;
    }
    

    public ConsoleType getConsoleType() {
        return consoleType;
    }

    // Getter
    public PromotionResponseDto getPromotion() {
        return promotion;
    }
/* 
    public WishlistResponseDto getWishlistResponseDto(){
        return this.wishlist;
    }

    public PromotionResponseDto getPromotionResponseDto(){
        return this.promotion;
    }
*/
}
