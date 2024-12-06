package ca.mcgill.ecse321.videogamessystem.dto.SpecificGameDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SpecificGameRequestDto {

    @NotNull(message = "Availability status must be specified.")
    private boolean availability;

    @Positive(message = "Serial number must be positive.")
    private int serialNumber;

    @NotNull(message = "Game ID cannot be null.")
    @Positive(message = "Game ID must be a positive number.")
    private Long gameId;

    public SpecificGameRequestDto(boolean availability, int serialNumber, Long gameId) {
        this.availability = availability;
        this.serialNumber = serialNumber;
        this.gameId = gameId;
    }

    protected SpecificGameRequestDto(){}

    // Getters and Setters
    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
