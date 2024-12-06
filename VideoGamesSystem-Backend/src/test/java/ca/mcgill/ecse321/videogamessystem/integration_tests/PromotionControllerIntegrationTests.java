package ca.mcgill.ecse321.videogamessystem.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.videogamessystem.VideogamessystemApplication;
import ca.mcgill.ecse321.videogamessystem.dto.PromotionDto.PromotionRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.PromotionDto.PromotionResponseDto;
import java.sql.Date;
import java.time.LocalDate;

@SpringBootTest(classes = VideogamessystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PromotionControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final int VALID_PERCENTAGE = 20;
    private static final Date VALID_START_DATE = Date.valueOf(LocalDate.now());
    private static final Date VALID_END_DATE = Date.valueOf(LocalDate.now().plusDays(15));
    private Long promotionId;

    @AfterAll
    public void clearDatabase() {
        restTemplate.delete("/promotions/" + promotionId);
    }

    @Test
    @Order(1)
    public void testCreatePromotion() {
        // Arrange
        PromotionRequestDto request = new PromotionRequestDto(VALID_PERCENTAGE, VALID_START_DATE, VALID_END_DATE);

        // Act
        ResponseEntity<PromotionResponseDto> response = restTemplate.postForEntity("/promotions", request, PromotionResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        promotionId = response.getBody().getId();
        assertTrue(promotionId > 0, "Promotion ID should be greater than 0.");
        assertEquals(VALID_PERCENTAGE, response.getBody().getPercentage());
        // assertEquals(VALID_START_DATE, response.getBody().getStartDate());
        // assertEquals(VALID_END_DATE, response.getBody().getEndDate());
    }

    @Test
    @Order(2)
    public void testGetPromotionById() {
        // Arrange
        String url = String.format("/promotions/%d", promotionId);

        // Act
        ResponseEntity<PromotionResponseDto> response = restTemplate.getForEntity(url, PromotionResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(promotionId, response.getBody().getId());
        assertEquals(VALID_PERCENTAGE, response.getBody().getPercentage());
        // assertEquals(VALID_START_DATE, response.getBody().getStartDate());
        // assertEquals(VALID_END_DATE, response.getBody().getEndDate());
    }

    @Test
    @Order(3)
    public void testGetAllPromotions() {
        // Act
        ResponseEntity<PromotionResponseDto[]> response = restTemplate.getForEntity("/promotions", PromotionResponseDto[].class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0, "The list of promotions should not be empty.");
    }

    @Test
    @Order(4)
    public void testIsPromotionAvailable() {
        // Arrange
        String url = String.format("/promotions/%d/available", promotionId);

        // Act
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody(), "The promotion should be available.");
    }

    @Test
    @Order(5)
    public void testDeletePromotion() {
        // Act
        restTemplate.delete("/promotions/" + promotionId);

        // Assert
        ResponseEntity<PromotionResponseDto> response = restTemplate.getForEntity("/promotions/" + promotionId, PromotionResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
