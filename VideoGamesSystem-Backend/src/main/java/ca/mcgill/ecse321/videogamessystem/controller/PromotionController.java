package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.PromotionDto.PromotionRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.PromotionDto.PromotionResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.Promotion;
import ca.mcgill.ecse321.videogamessystem.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    /**
     * Create a new promotion.
     *
     * @param request the promotion request DTO containing the promotion details
     * @return the response DTO with the created promotion details
     */
    @PostMapping("/promotions")
    public PromotionResponseDto createPromotion(@Valid @RequestBody PromotionRequestDto request) {
        Promotion promotion = promotionService.createPromotion(
                request.getPercentage(),
                request.getStartDate(),
                request.getEndDate()
        );
        return new PromotionResponseDto(promotion);
    }

    /**
     * Retrieve a promotion by its ID.
     *
     * @param id the ID of the promotion
     * @return the response DTO with the promotion details
     */
    @GetMapping("/promotions/{id}")
    public PromotionResponseDto getPromotionById(@PathVariable Long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        return new PromotionResponseDto(promotion);
    }

    /**
     * Retrieve all promotions.
     *
     * @return a list of response DTOs with the details of all promotions
     */
    @GetMapping("/promotions")
    public List<PromotionResponseDto> getAllPromotions() {
        return promotionService.getAllPromotion().stream()
                .map(PromotionResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Delete a promotion by its ID.
     *
     * @param id the ID of the promotion to delete
     */
    @DeleteMapping("/promotions/{id}")
    public void deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
    }

    /**
     * Check if a promotion is currently available.
     *
     * @param id the ID of the promotion
     * @return true if the promotion is currently available, false otherwise
     */
    @GetMapping("/promotions/{id}/available")
    public boolean isPromotionAvailable(@PathVariable Long id) {
        return promotionService.promotionAvailable(id);
    }
}