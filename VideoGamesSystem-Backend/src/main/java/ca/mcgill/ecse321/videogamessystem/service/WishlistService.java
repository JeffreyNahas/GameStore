package ca.mcgill.ecse321.videogamessystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Customer;
import ca.mcgill.ecse321.videogamessystem.model.Wishlist;
import ca.mcgill.ecse321.videogamessystem.repository.WishlistRepository;

@Service
public class WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;

    /**
     * @param id
     * @return
     */
    @Transactional
    public Wishlist getWishlistById(Long id) {
        Wishlist wishlist = wishlistRepository.findWishlistById(id);
        if (wishlist == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Wishlist not found.");
        }
        return wishlist;
    }

    /**
     * @param customer
     * @return
     */
    @Transactional
    public Wishlist getWishlistByCustomer(Customer customer) {
        if (customer == null) {
            throw new VideoGamesSystemException(HttpStatus.NOT_FOUND,"Customer cannot be null.");
        }
        return wishlistRepository.findWishlistByCustomer(customer);
    }

}


