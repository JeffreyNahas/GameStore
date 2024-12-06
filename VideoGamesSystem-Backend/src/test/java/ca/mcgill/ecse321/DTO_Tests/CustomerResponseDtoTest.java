// package ca.mcgill.ecse321.DTO_Tests;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerResponseDto;
// import ca.mcgill.ecse321.videogamessystem.model.Customer;

// public class CustomerResponseDtoTest {

//     @Test
//     public void testCustomerResponseDtoCreation() {
//         // Create a Customer model with all fields initialized
//         Customer customer = new Customer();
//         // customer.setId(1L);
//         customer.setUserName("testUser");
//         customer.setEmail("test@example.com");
//         customer.setPhoneNumber(123456);
//         customer.setAddress("ma maisonnette");
    
//         // Initialize CustomerResponseDto from the model
//         CustomerResponseDto dto = new CustomerResponseDto(customer);
    
//         // // Debug output
//         // // System.out.println("Customer ID: " + customer.getId());
//         // System.out.println("DTO ID: " + dto.getId());
    
//         // Assertions
//         // assertEquals(1L, dto.getId(), "ID mismatch");
//         assertEquals("testUser", dto.getUserName(), "Username mismatch");
//         assertEquals("test@example.com", dto.getEmail(), "Email mismatch");
//         assertEquals(123456, dto.getPhoneNumber(), "Phone number mismatch");
//         assertEquals("ma maisonnette", dto.getAddress(), "Address mismatch");
//     }
    

//     @Test
//     public void testCustomerResponseDtoSetters() {
//         CustomerResponseDto dto = new CustomerResponseDto();

//         dto.setId(2L);
//         dto.setUserName("newUser");
//         dto.setEmail("new@example.com");
//         dto.setPhoneNumber(654321);
//         dto.setAddress("new address");

//         assertEquals(2L, dto.getId());
//         assertEquals("newUser", dto.getUserName());
//         assertEquals("new@example.com", dto.getEmail());
//         assertEquals(654321, dto.getPhoneNumber());
//         assertEquals("new address", dto.getAddress());
//     }
// }
