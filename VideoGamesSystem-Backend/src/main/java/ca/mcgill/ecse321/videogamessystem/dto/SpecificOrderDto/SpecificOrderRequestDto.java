package ca.mcgill.ecse321.videogamessystem.dto.SpecificOrderDto;

import java.sql.Date;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;

public class SpecificOrderRequestDto {

    @NotNull(message = "Order date cannot be null.")
    @PastOrPresent(message = "Order date cannot be in the future.")
    private Date orderDate;

    @Positive(message = "Card number must be a positive number.")
    private int cardNumber;

    @Positive(message = "Customer ID must be a positive number.")
    private Long customerId;

    // Constructors
    public SpecificOrderRequestDto() {
    }

    public SpecificOrderRequestDto(Date orderDate, int cardNumber, Long customerId) {
        this.orderDate = orderDate;
        this.cardNumber = cardNumber;
        this.customerId = customerId;
    }

    // Getters and Setters
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "OrderRequestDTO{" +
                "orderDate=" + orderDate +
                ", cardNumber=" + cardNumber +
                ", customerId=" + customerId +
                '}';
    }
}
