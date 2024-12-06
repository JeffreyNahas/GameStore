package ca.mcgill.ecse321.videogamessystem.integration_tests;

import static org.junit.jupiter.api.Assertions.*;

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
import ca.mcgill.ecse321.videogamessystem.dto.StaffDto.StaffRequestDto;
import ca.mcgill.ecse321.videogamessystem.dto.StaffDto.StaffResponseDto;
import ca.mcgill.ecse321.videogamessystem.repository.StaffRepository;

@SpringBootTest(classes = VideogamessystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class StaffControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StaffRepository staffRepository;

    private static final String VALID_USERNAME = "staffuser";
    private static final String VALID_EMAIL = "staffuser@example.com";
    private static final String VALID_PASSWORD = "securepassword";
    private static final boolean VALID_ADMIN = false;

    private static final String UPDATED_USERNAME = "updatedstaffuser";
    private static final String UPDATED_EMAIL = "updatedstaffuser@example.com";

    private Long staffId;

    @AfterAll
    public void clearDatabase() {
        staffRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateStaff() {
        // Arrange
        StaffRequestDto request = new StaffRequestDto(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD, VALID_ADMIN);

        // Act
        ResponseEntity<StaffResponseDto> response = restTemplate.postForEntity("/staff", request, StaffResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0, "The ID should be positive.");
        this.staffId = response.getBody().getId();
        assertEquals(VALID_USERNAME, response.getBody().getUserName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_ADMIN, response.getBody().getAdmin());
    }

    @Test
    @Order(2)
    public void testGetStaffById() {
        // Arrange
        String url = String.format("/staff/%d", this.staffId);

        // Act
        ResponseEntity<StaffResponseDto> response = restTemplate.getForEntity(url, StaffResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(this.staffId, response.getBody().getId());
        assertEquals(VALID_USERNAME, response.getBody().getUserName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_ADMIN, response.getBody().getAdmin());
    }

    @Test
    @Order(3)
    public void testGetStaffByUserName() {
        // Arrange
        String url = String.format("/staff/username/%s", VALID_USERNAME);

        // Act
        ResponseEntity<StaffResponseDto> response = restTemplate.getForEntity(url, StaffResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(VALID_USERNAME, response.getBody().getUserName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(4)
    public void testGetStaffByEmail() {
        // Arrange
        String url = String.format("/staff/email/%s", VALID_EMAIL);

        // Act
        ResponseEntity<StaffResponseDto> response = restTemplate.getForEntity(url, StaffResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(VALID_USERNAME, response.getBody().getUserName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
    }

    @Test
    @Order(5)
    public void testDeleteStaff() {
        // Arrange
        String url = String.format("/staff/%d", this.staffId);

        // Act
        restTemplate.delete(url);

        // Assert
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // @Test
    // @Order(6)
    // public void testCreateStaffWithExistingUsername() {
    //     // Arrange
    //     StaffRequestDto request = new StaffRequestDto(VALID_USERNAME, "newemail@example.com", "newpassword", false);

    //     // Act
    //     ResponseEntity<StaffResponseDto> response = restTemplate.postForEntity("/staff", request, StaffResponseDto.class);

    //     // Assert
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    //     assertTrue(response.getBody().getMessage().contains("UserName already exists"));
    // }

    // @Test
    // @Order(7)
    // public void testCreateStaffWithInvalidEmail() {
    //     // Arrange
    //     StaffRequestDto request = new StaffRequestDto(VALID_USERNAME, "invalidemail", "password", false);

    //     // Act
    //     ResponseEntity<StaffResponseDto> response = restTemplate.postForEntity("/staff", request, StaffResponseDto.class);

    //     // Assert
    //     assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    //     assertTrue(response.getBody().getMessage().contains("invalid email"));
    // }
}
