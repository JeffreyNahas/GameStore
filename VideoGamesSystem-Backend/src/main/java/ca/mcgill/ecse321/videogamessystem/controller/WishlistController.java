package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.WishlistDto.WishlistResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Wishlist;
import ca.mcgill.ecse321.videogamessystem.service.WishlistService;
import ca.mcgill.ecse321.videogamessystem.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CustomerService customerService;

    /**
     * Get a wishlist by its ID.
     * 
     * @param id the ID of the wishlist.
     * @return WishlistResponseDto with the requested wishlist details.
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/wishlists/{id}")
    public WishlistResponseDto getWishlistById(@PathVariable Long id) {
        Wishlist wishlist = wishlistService.getWishlistById(id);
        return new WishlistResponseDto(wishlist);
    }

    /**
     * Get a wishlist by customer ID.
     * 
     * @param customerId the ID of the customer.
     * @return WishlistResponseDto with the details of the wishlist for the specified customer.
     */
    @GetMapping("/wishlists/customer/{customerId}")
    public WishlistResponseDto getWishlistByCustomerId(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        Wishlist wishlist = wishlistService.getWishlistByCustomer(customer);
        return new WishlistResponseDto(wishlist);
    }

}
