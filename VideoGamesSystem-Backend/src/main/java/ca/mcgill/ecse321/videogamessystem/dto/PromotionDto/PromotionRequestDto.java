package ca.mcgill.ecse321.videogamessystem.dto.PromotionDto;

import java.sql.Date;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
//import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class PromotionRequestDto {

    @NotNull(message = "Percentage cannot be null")
    @Positive(message = "Percentage must be positive")
    @Min(0)
    @Max(100)
    private int percentage;

    @NotNull(message = "Start date cannot be null")
 //   @FutureOrPresent(message = "Start date must be today or in the future")
    private Date startDate;

    @NotNull(message = "End date cannot be null")
 //   @FutureOrPresent(message = "End date must be today or in the future")
    private Date endDate;

    // Constructor
    public PromotionRequestDto(int percentage, Date startDate, Date endDate) {
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PromotionRequestDto() {}

    // Getters and Setters
    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
