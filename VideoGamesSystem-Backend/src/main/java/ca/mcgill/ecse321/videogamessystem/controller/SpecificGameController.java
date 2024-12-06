package ca.mcgill.ecse321.videogamessystem.controller;

import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto.SpecificGameResponseDto;
import ca.mcgill.ecse321.videogamessystem.model.SpecificGame;
import ca.mcgill.ecse321.videogamessystem.model.SpecificOrder;
import ca.mcgill.ecse321.videogamessystem.service.SpecificGameService;
import ca.mcgill.ecse321.videogamessystem.service.SpecificOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SpecificGameController {

    @Autowired
    private SpecificGameService specificGameService;

    @Autowired
    private SpecificOrderService specificOrderService;

    /**
     * Creates a new specific game instance with provided details.
     *
     * @param specificGameRequestDto the DTO containing specific game data
     * @return a ResponseEntity with the created SpecificGameResponseDto
     */
    @PostMapping("/specificGames")
    public ResponseEntity<SpecificGameResponseDto> createSpecificGame(@Valid @RequestBody SpecificGameRequestDto specificGameRequestDto) {
        SpecificGame specificGame = specificGameService.createSpecificGame(
                specificGameRequestDto.getSerialNumber(),
                specificGameRequestDto.isAvailability(),
                specificGameRequestDto.getGameId()
        );
        return ResponseEntity.ok(convertToDto(specificGame));
    }

    /**
     * Retrieves a specific game by its serial number.
     *
     * @param serialNumber the serial number of the specific game to retrieve
     * @return a ResponseEntity with the retrieved SpecificGameResponseDto
     */
    @GetMapping("/specificGames/{serialNumber}")
    public ResponseEntity<SpecificGameResponseDto> getSpecificGameBySerialNumber(@PathVariable int serialNumber) {
        SpecificGame specificGame = specificGameService.getSpecificGameBySerialNumber(serialNumber);
        return ResponseEntity.ok(convertToDto(specificGame));
    }

    /**
     * Retrieves all specific games.
     *
     * @return a ResponseEntity containing a list of all SpecificGameResponseDtos
     */
    @GetMapping("/specificGames")
    public ResponseEntity<List<SpecificGameResponseDto>> getAllSpecificGames() {
        List<SpecificGameResponseDto> specificGames = specificGameService.getAllSpecificGames()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(specificGames);
    }

    /**
     * Updates the availability status of a specific game.
     *
     * @param serialNumber   the serial number of the specific game to update
     * @param newAvailability the new availability status to set
     * @return a ResponseEntity with the updated SpecificGameResponseDto
     */
    @PutMapping("/specificGames/{serialNumber}/availability")
    public ResponseEntity<SpecificGameResponseDto> updateAvailability(
            @PathVariable int serialNumber,
            @RequestBody boolean newAvailability) {
        SpecificGame specificGame = specificGameService.updateAvailability(serialNumber, newAvailability);
        return ResponseEntity.ok(convertToDto(specificGame));
    }

    /**
     * Adds a specific game to an order.
     *
     * @param serialNumber the serial number of the specific game to add to the order
     * @param orderId the ID of the order to add the specific game to
     * @return a ResponseEntity containing a list of SpecificGameResponseDtos associated with the updated order
     */
    @PutMapping("/specificGames/{serialNumber}/addToOrder")
    public ResponseEntity<List<SpecificGameResponseDto>> addSpecificGameToOrder(
            @PathVariable int serialNumber,
            @RequestParam int orderId) {
        SpecificOrder order = specificOrderService.getOrderById(orderId);
        List<SpecificGame> updatedSpecificGames = specificGameService.addSpecificGameToOrder(serialNumber, order);
        List<SpecificGameResponseDto> responseDtos = updatedSpecificGames.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Removes a specific game from an order.
     *
     * @param serialNumber the serial number of the specific game to remove from the order
     * @param orderId the ID of the order from which to remove the specific game
     * @return a ResponseEntity containing a list of SpecificGameResponseDtos associated with the updated order
     */
    @PutMapping("/specificGames/{serialNumber}/removeFromOrder")
    @Transactional
    public ResponseEntity<List<SpecificGameResponseDto>> removeSpecificGameFromOrder(
            @PathVariable int serialNumber,
            @RequestParam int orderId) {
        SpecificOrder order = specificOrderService.getOrderById(orderId);
        List<SpecificGame> updatedSpecificGames = specificGameService.removeSpecificGameFromOrder(serialNumber, order);
        List<SpecificGameResponseDto> responseDtos = updatedSpecificGames.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Deletes a specific game by its serial number.
     *
     * @param serialNumber the serial number of the specific game to delete
     * @return a ResponseEntity with no content after successful deletion
     */
    @DeleteMapping("/specificGames/{serialNumber}")
    public ResponseEntity<Void> deleteSpecificGame(@PathVariable int serialNumber) {
        specificGameService.deleteSpecificGame(serialNumber);
        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to convert a SpecificGame entity to a SpecificGameResponseDto.
     *
     * @param specificGame the SpecificGame entity to convert
     * @return the converted SpecificGameResponseDto
     */
    private SpecificGameResponseDto convertToDto(SpecificGame specificGame) {
        return new SpecificGameResponseDto(specificGame);
    }
}