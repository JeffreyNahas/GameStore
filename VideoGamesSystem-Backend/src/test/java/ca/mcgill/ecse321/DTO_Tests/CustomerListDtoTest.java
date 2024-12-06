// package ca.mcgill.ecse321.DTO_Tests;

// import org.junit.jupiter.api.Test;
// import java.util.Arrays;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerListDto;
// import ca.mcgill.ecse321.videogamessystem.dto.CustomerDto.CustomerSummaryDto;
// import ca.mcgill.ecse321.videogamessystem.model.Customer;

// public class CustomerListDtoTest {

//     @Test
//     public void testCustomerListDtoCreation() {

//         Customer customera = new Customer("paolo", "paolo@paolo.com", "1234", 12345, "123 ch");
//         Customer customerb = new Customer("paolo2", "paolo2@paolo.com", "1234", 123345, "123 ch");

//         CustomerSummaryDto customer1 = new CustomerSummaryDto(customera);
//         CustomerSummaryDto customer2 = new CustomerSummaryDto(customerb);

//         List<CustomerSummaryDto> customers = Arrays.asList(customer1, customer2);

//         CustomerListDto dto = new CustomerListDto(customers);

//         assertEquals(2, dto.getCustomers().size());
//         assertEquals("paolo", dto.getCustomers().get(0).GetUserName());
//         assertEquals("paolo2", dto.getCustomers().get(1).GetUserName());
//     }

// //     @Test
// //     public void testCustomerListDtoSetters() {
// //         Customer customera = new Customer("paolo", "paolo@paolo.com", "1234", 12345, "123 ch");
// //         Customer customerb = new Customer("paolo2", "paolo2@paolo.com", "1234", 123345, "123 ch");;
// //         CustomerSummaryDto customer1 = new CustomerSummaryDto(customera);
// //         CustomerSummaryDto customer2 = new CustomerSummaryDto(customerb);

// //         CustomerListDto dto = new CustomerListDto();
// //         dto.setCustomers(Arrays.asList(customer1, customer2));

// //         assertEquals(2, dto.getCustomers().size());
// //         assertEquals("user1", dto.getCustomers().get(0).GetUserName());
// //         assertEquals("user2", dto.getCustomers().get(1).GetUserName());
// //     }
// }
