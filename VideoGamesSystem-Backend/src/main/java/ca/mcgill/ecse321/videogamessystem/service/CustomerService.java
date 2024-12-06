package ca.mcgill.ecse321.videogamessystem.service;

import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Wishlist;
import ca.mcgill.ecse321.videogamessystem.model.Review;
import ca.mcgill.ecse321.videogamessystem.repository.CustomerRepository;
import ca.mcgill.ecse321.videogamessystem.repository.WishlistRepository;
import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    private static final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    @Transactional
    public Customer createCustomer(String userName, String email, String password, int phoneNumber, String address) {
        if (customerRepository.findCustomerByUserName(userName) != null) {
            throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Username already exists");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        }
        if (!Pattern.compile(emailRegex).matcher(email).matches()) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }
        if (customerRepository.findCustomerByEmail(email) != null) {
            throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Email already exists");
        }

        if (password == null || password.trim().length() < 4) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters");
        }

        if (phoneNumber < 1111) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Phone number must contain more digits");
        }
        // if (customerRepository.findCustomerByPhoneNumber(phoneNumber) != null) {
        //     throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Phone number already exists");
        // }

        if (address == null || address.trim().isEmpty()) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Address cannot be empty");
        }

        Customer customer = new Customer(userName, email, password, phoneNumber, address);

        Wishlist newWishlist = new Wishlist();
        newWishlist.setCustomer(customer);

        wishlistRepository.save(newWishlist);

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Customer with ID " + id + " not found"));
    }

    @Transactional
    public Customer getCustomerByUserName(String userName) {
        return customerRepository.findCustomerByUserName(userName);
    }

    @Transactional
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    @Transactional
    public Customer updateCustomerUserName(Long id, String newUserName) {
        Customer customer = getCustomerById(id);
        if (newUserName == null || newUserName.trim().isEmpty()) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }
        if (customerRepository.findCustomerByUserName(newUserName) != null) {
            throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Username already exists");
        }
        customer.setUserName(newUserName);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomerEmail(Long id, String newEmail) {
        Customer customer = getCustomerById(id);
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new VideoGamesSystemException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        }
        if (customerRepository.findCustomerByEmail(newEmail) != null) {
            throw new VideoGamesSystemException(HttpStatus.CONFLICT, "Email already exists");
        }
        customer.setEmail(newEmail);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomerPhoneNumber(Long id, int newPhoneNumber) {
        Customer customer = getCustomerById(id);
        customer.setPhoneNumber(newPhoneNumber);
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomerAddress(Long id, String newAddress) {
        Customer customer = getCustomerById(id);
        customer.setAddress(newAddress);
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    @Transactional
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Transactional
    public Customer getCustomerByReview(Review review) {
        Customer customer = review.getCustomer();
        if (customer == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND, "Review does not belong to any customer");
        }
        return customer;
    }

    @Transactional
    public Customer getCustomerByWishlist(Long whishlistID) {
        Wishlist wishlist = wishlistRepository.findWishlistById(whishlistID);

        Customer customer = wishlist.getCustomer();
        if (customer == null){
            throw new IllegalArgumentException("the wishlist has no associated customer");
        }
        return customer;
    }


}