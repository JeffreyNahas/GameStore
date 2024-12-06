package ca.mcgill.ecse321.videogamessystem.dto.SpecificOrderDto;

import java.sql.Date;

import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;

public class SpecificOrderResponseDto {

    private int orderNumber;
    private Date orderDate;
    private int cardNumber;
    private Customer customer;

    

    // Constructor with all attributes
    protected SpecificOrderResponseDto() {
        
    }

    // Constructor that initializes from SpecificOrder
    public SpecificOrderResponseDto(SpecificOrder order) {
        this.orderNumber = order.getNumber();
        this.orderDate = order.getOrderDate();
        this.cardNumber = order.getCardNumber();
        this.customer = (order.getCustomer() != null) ? order.getCustomer() : null;
    }

    // Getters
    public int getOrderNumber() {
        return orderNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    // Setters
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "OrderResponseDTO{" +
                "orderNumber=" + orderNumber +
                ", orderDate=" + orderDate +
                ", cardNumber=" + cardNumber +
                ", customer=" + customer +
                '}';
    }
}
