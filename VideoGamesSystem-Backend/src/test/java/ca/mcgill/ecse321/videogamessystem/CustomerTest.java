// package ca.mcgill.ecse321.videogamessystem;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;

// import java.sql.Date;
// import org.springframework.boot.test.context.SpringBootTest;

// import ca.mcgill.ecse321.videogamessystem.model.Customer;
// import ca.mcgill.ecse321.videogamessystem.model.Wishlist;
// import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
// import ca.mcgill.ecse321.videogamessystem.repository.WishlistRepository;

// @SpringBootTest
// public class CustomerTest {
// 	@Autowired
// 	private CustomerRepository customerRepository;

// 	@Autowired
//     private WishlistRepository wishlistRepository;

// 	@AfterEach
// 	public void clearDatabase() {
// 		customerRepository.deleteAll();
// 	}

// 	@Test
// 	public void testPersistAndLoadCustomer() {

// 		// Create person
// 		String name = "Muffin Man";
// 		String emailAddress = "muffin.man@gmail.com";
// 		String password = "i_love_muffins";
// 		int phoneNumber = 123783;
// 		String address= "abc";
// 		Customer muffinMan = new Customer();
// 		muffinMan.setUserName(name);
// 		muffinMan.setEmail(emailAddress);
// 		muffinMan.setPassword(password);
// 		muffinMan.setPhoneNumber(phoneNumber);
// 		muffinMan.setAddress(address);

// 		// Save person
// 		muffinMan = customerRepository.save(muffinMan);

// 		Wishlist wishlist = new Wishlist();
// 		wishlist.setCustomer(muffinMan);
// 		wishlist = wishlistRepository.save(wishlist);

// 		muffinMan.setWishlist(wishlist);
// 		muffinMan = customerRepository.save(muffinMan);
		
// 		long muffinId = muffinMan.getId();

// 		// Read person from database
// 		Customer muffinManFromDb = customerRepository.findCustomerById(muffinId);

// 		// Assert correct response
// 		assertNotNull(muffinMan);
// 		assertEquals(muffinManFromDb.getUserName(), name);
// 		assertEquals(muffinManFromDb.getEmail(), emailAddress);
// 		assertEquals(muffinManFromDb.getPassword(), password);
// 	}
// }
