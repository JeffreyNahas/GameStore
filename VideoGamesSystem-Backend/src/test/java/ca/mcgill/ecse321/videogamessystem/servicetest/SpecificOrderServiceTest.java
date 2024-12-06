package ca.mcgill.ecse321.videogamessystem.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;
import ca.mcgill.ecse321.videogamessystem.repository.SpecificOrderRepository;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
import ca.mcgill.ecse321.videogamessystem.service.SpecificOrderService;
import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecificOrderServiceTest {

    @Mock
    private SpecificOrderRepository specificOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private SpecificOrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSpecificOrder_Success() {
        Date orderDate = new Date(System.currentTimeMillis());
        int cardNumber = 12345678;
        Long customerId = 1L;
        Customer customer = new Customer();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        SpecificOrder order = new SpecificOrder(0, orderDate, cardNumber);
        order.setCustomer(customer);
        when(specificOrderRepository.save(any(SpecificOrder.class))).thenReturn(order);

        SpecificOrder createdOrder = orderService.createSpecificOrder(orderDate, cardNumber, customerId);

        assertNotNull(createdOrder);
        assertEquals(orderDate, createdOrder.getOrderDate());
        assertEquals(cardNumber, createdOrder.getCardNumber());
        assertEquals(customer, createdOrder.getCustomer());
    }

    @Test
    public void testCreateSpecificOrder_InvalidDate() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));

        Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
            orderService.createSpecificOrder(futureDate, 123456, customerId);
        });

        assertEquals("Order date cannot be in the future.", exception.getMessage());
    }

    @Test
    public void testGetOrderById_OrderExists() {
        int orderId = 1;
        SpecificOrder order = new SpecificOrder();
        when(specificOrderRepository.findOrderByNumber(orderId)).thenReturn(order);

        SpecificOrder foundOrder = orderService.getOrderById(orderId);

        assertNotNull(foundOrder);
        verify(specificOrderRepository, times(1)).findOrderByNumber(orderId);
    }

    @Test
    public void testGetOrderById_OrderNotFound() {
        int orderId = 1;
        when(specificOrderRepository.findOrderByNumber(orderId)).thenReturn(null);

        Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
            orderService.getOrderById(orderId);
        });

        assertEquals("Order not found.", exception.getMessage());
    }

    @Test
    public void testUpdateCardNumber_Success() {
        int orderNumber = 1;
        int newCardNumber = 12345678;
        SpecificOrder order = new SpecificOrder();
        order.setCardNumber(87654321);

        when(specificOrderRepository.findOrderByNumber(orderNumber)).thenReturn(order);
        when(specificOrderRepository.save(order)).thenReturn(order);

        SpecificOrder updatedOrder = orderService.updateCardNumber(orderNumber, newCardNumber);

        assertNotNull(updatedOrder);
        assertEquals(newCardNumber, updatedOrder.getCardNumber());
        verify(specificOrderRepository, times(1)).save(order);
    }

    @Test
    public void testUpdateCardNumber_InvalidCardNumber() {
        int orderNumber = 1;
        int invalidCardNumber = -12345;
        SpecificOrder order = new SpecificOrder();

        when(specificOrderRepository.findOrderByNumber(orderNumber)).thenReturn(order);

        Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
            orderService.updateCardNumber(orderNumber, invalidCardNumber);
        });

        assertEquals("Card number must be valid.", exception.getMessage());
    }

    @Test
    public void testDeleteOrder_Success() {
        int orderId = 1;
        SpecificOrder order = new SpecificOrder();
        when(specificOrderRepository.findOrderByNumber(orderId)).thenReturn(order);

        SpecificOrder deletedOrder = orderService.deleteOrder(orderId);

        assertNotNull(deletedOrder);
        verify(specificOrderRepository, times(1)).delete(order);
    }

    @Test
    public void testDeleteOrder_NotFound() {
        int orderId = 1;

        when(specificOrderRepository.findOrderByNumber(orderId)).thenReturn(null);

        Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
            orderService.deleteOrder(orderId);
        });

        assertEquals("Order not found.", exception.getMessage());
    }

    @Test
    public void testGetOrdersByCustomer() {
        Long customerId = 1L;
        Customer customer = new Customer();
        List<SpecificOrder> orders = new ArrayList<>();
        orders.add(new SpecificOrder());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(specificOrderRepository.findOrderByCustomer(customer)).thenReturn(orders);

        List<SpecificOrder> customerOrders = orderService.getOrdersByCustomer(customerId);

        assertNotNull(customerOrders);
        assertEquals(1, customerOrders.size());
    }

    @Test
    public void testPlaceNewOrder_Success() {
        int orderID = 1;
        SpecificOrder order = new SpecificOrder();
        Customer customer = new Customer();

        when(specificOrderRepository.findOrderByNumber(orderID)).thenReturn(order);
        when(specificOrderRepository.save(any(SpecificOrder.class))).thenReturn(order);

        SpecificOrder updatedOrder = orderService.placeNewOrder(orderID, customer);

        assertNotNull(updatedOrder);
        assertEquals(customer, updatedOrder.getCustomer());
        verify(specificOrderRepository, times(1)).save(order);
    }

    @Test
    public void testPlaceNewOrder_OrderNotFound() {
        int orderID = 1;
        Customer customer = new Customer();

        when(specificOrderRepository.findOrderByNumber(orderID)).thenReturn(null);

        Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
            orderService.placeNewOrder(orderID, customer);
        });

        assertEquals("order not found", exception.getMessage());
    }

    @Test
    public void testPlaceNewOrder_CustomerIsNull() {
        int orderID = 1;
        SpecificOrder order = new SpecificOrder();

        when(specificOrderRepository.findOrderByNumber(orderID)).thenReturn(order);

        Exception exception = assertThrows(VideoGamesSystemException.class, () -> {
            orderService.placeNewOrder(orderID, null);
        });

        assertEquals("customer not found", exception.getMessage());
    }
}
