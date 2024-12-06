package ca.mcgill.ecse321.videogamessystem.dto.PromotionDto;

import ca.mcgill.ecse321.videogamessystem.model.Promotion;

import java.sql.Date;

public class PromotionResponseDto {
    private Long id;
    private int percentage;
    private Date startDate;
    private Date endDate;


    public PromotionResponseDto(Promotion promotion) {
        this.id = promotion.getId();
        this.percentage = promotion.getPercentage();
        this.startDate = promotion.getStartDate();
        this.endDate = promotion.getEndDate();

    }

    protected PromotionResponseDto() {}


    // Getters and Setters
    public Long getId() {
        return id;
    }

    

    public int getPercentage() {
        return percentage;
    }

    

    public Date getStartDate() {
        return startDate;
    }

    

    public Date getEndDate() {
        return endDate;
    }

    public static PromotionResponseDto convertToPromotionResponseDto(Promotion promotion){
        if (promotion == null){
            throw new IllegalArgumentException("Promotion cannot be null.");
        }

        PromotionResponseDto dto = new PromotionResponseDto(promotion);
        return dto;
    }
}
