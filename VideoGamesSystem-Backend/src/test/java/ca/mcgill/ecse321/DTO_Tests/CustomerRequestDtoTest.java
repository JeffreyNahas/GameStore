// package ca.mcgill.ecse321.DTO_Tests;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerRequestDto;

// public class CustomerRequestDtoTest {

//     @Test
//     public void testCustomerRequestDtoCreation() {

//         String username = "testUser";
//         String email = "test@example.com";
//         String password = "securePassword";
//         int phoneNumber = 123456;
//         String address = "ma maisonnette";
//         Long wishlistId = 1L;
//         CustomerRequestDto dto = new CustomerRequestDto(username, email, password, phoneNumber, address, wishlistId);

//         assertEquals(username, dto.getUserName());
//         assertEquals(email, dto.getEmail());
//         assertEquals(password, dto.getPassword());
//     }

//     @Test
//     public void testCustomerRequestDtoSetters() {
//         CustomerRequestDto dto = new CustomerRequestDto();
        
//         dto.setUserName("newUser");
//         dto.setEmail("new@example.com");
//         dto.setPassword("newPassword");

//         assertEquals("newUser", dto.getUserName());
//         assertEquals("new@example.com", dto.getEmail());
//         assertEquals("newPassword", dto.getPassword());
//     }
// }
