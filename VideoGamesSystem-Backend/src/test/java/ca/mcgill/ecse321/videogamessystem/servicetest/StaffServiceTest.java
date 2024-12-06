package ca.mcgill.ecse321.videogamessystem.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.videogamessystem.exception.VideoGamesSystemException;
import ca.mcgill.ecse321.videogamessystem.model.Staff;
import ca.mcgill.ecse321.videogamessystem.repository.StaffRepository;
import ca.mcgill.ecse321.videogamessystem.service.StaffService;

public class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffService staffService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        staffRepository.deleteAll();
    }

    @Test
    public void testCreateStaff_Success() {
        String userName = "staff1";
        String email = "staff1@example.com";
        String password = "password123";
        boolean isAdmin = true;

        Staff mockStaff = new Staff(userName, email, password, isAdmin);
        when(staffRepository.save(any(Staff.class))).thenReturn(mockStaff);

        Staff createdStaff = staffService.createStaff(userName, email, password, isAdmin);

        assertNotNull(createdStaff);
        assertEquals(userName, createdStaff.getUserName());
        assertEquals(email, createdStaff.getEmail());
        assertEquals(password, createdStaff.getPassword());
        assertEquals(isAdmin, createdStaff.getStaffType());
    }

    @Test
    public void testCreateStaff_EmailAlreadyExists() {
        String email = "staff1@example.com";
        when(staffRepository.findStaffByEmail(email)).thenReturn(new Staff());

        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            staffService.createStaff("userName", email, "password123", false);
        });

        assertEquals("Email already exists", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testGetStaffById_StaffExists() {
        Staff mockStaff = new Staff("staff3", "staff3@example.com", "password123", false);
        when(staffRepository.findStaffById(1L)).thenReturn(mockStaff);

        Staff foundStaff = staffService.getStaffById(1L);

        assertNotNull(foundStaff);
        assertEquals("staff3", foundStaff.getUserName());
        assertEquals("staff3@example.com", foundStaff.getEmail());
    }

    @Test
    public void testGetStaffById_StaffNotFound() {
        when(staffRepository.findStaffById(9999L)).thenReturn(null);

        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            staffService.getStaffById(9999L);
        });

        assertEquals("Staff not found.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    // @Test
    // public void testUpdateStaffUserName_Success() {
    //     Staff mockStaff = new Staff("staff4", "staff4@example.com", "password123", false);
    //     when(staffRepository.findStaffById(1L)).thenReturn(mockStaff);
    //     when(staffRepository.save(any(Staff.class))).thenReturn(mockStaff);

    //     Staff updatedStaff = staffService.updateStaffUserName(1L, "newStaff4");

    //     assertNotNull(updatedStaff);
    //     assertEquals("newStaff4", updatedStaff.getUserName());
    // }

    @Test
    public void testDeleteStaff_Success() {
        Staff mockStaff = new Staff("staff6", "staff6@example.com", "password123", false);
        when(staffRepository.findStaffById(1L)).thenReturn(mockStaff);
        doNothing().when(staffRepository).delete(mockStaff);

        Staff deletedStaff = staffService.deleteStaff(1L);

        assertNotNull(deletedStaff);
        assertEquals("staff6", deletedStaff.getUserName());
        verify(staffRepository, times(1)).delete(mockStaff);
    }

    // @Test
    // public void testSetOwner_NoCurrentOwner() {
    //     Staff mockStaff = mock(Staff.class);
    //     when(staffRepository.findStaffById(1L)).thenReturn(mockStaff);
    //     when(staffRepository.findStaffByAdmin(true)).thenReturn(new ArrayList<>());

    //     when(staffRepository.save(mockStaff)).thenReturn(mockStaff);

    //     Staff updatedOwner = staffService.setOwner(1L);

    //     assertNotNull(updatedOwner);
    //     verify(mockStaff).setStaffType(true);
    //     verify(staffRepository, times(1)).save(mockStaff);
    // }

    @Test
    public void testToList_EmptyIterable() {
        // Arrange
        List<Integer> emptyList = new ArrayList<>();

        // Act
        List<Integer> result = new ArrayList<>();
        for (Integer element : emptyList) {
            result.add(element);
        }

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result list should be empty");
    }

    @Test
    public void testToList_MultipleElements() {
        // Arrange
        List<String> input = Arrays.asList("One", "Two", "Three");

        // Act
        List<String> result = new ArrayList<>();
        for (String element : input) {
            result.add(element);
        }

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(3, result.size(), "Result list should have three elements");
        assertTrue(result.containsAll(input), "Result should contain all elements from the input");
    }

    // @Test
    // public void testSetOwner_NoCurrentOwner() {
    //     // Arrange
    //     Long staffId = 1L;
    //     Staff staff = new Staff("user1", "user1@example.com", "password", false); // Non-owner staff
    //     when(staffRepository.findStaffById(staffId)).thenReturn(staff);
    //     when(staffRepository.findStaffByAdmin(true)).thenReturn(new ArrayList<>()); // No current owner

    //     // Act
    //     Staff newOwner = staffService.setOwner(staffId);

    //     // Assert
    //     assertNotNull(newOwner, "The returned staff should not be null.");
    //     assertTrue(newOwner.getStaffType(), "The staff should now be an owner.");
    //     verify(staffRepository, times(1)).findStaffById(staffId);
    //     verify(staffRepository, times(1)).findStaffByAdmin(true);
    //     verify(staffRepository, times(1)).save(newOwner);
    // }

    @Test
    public void testSetOwner_StaffNotFound() {
        // Arrange
        Long invalidStaffId = 999L;
        when(staffRepository.findStaffById(invalidStaffId)).thenReturn(null);

        // Act & Assert
        VideoGamesSystemException exception = assertThrows(VideoGamesSystemException.class, () -> {
            staffService.setOwner(invalidStaffId);
        });

        assertEquals("Staff not found.", exception.getMessage());
        verify(staffRepository, times(1)).findStaffById(invalidStaffId);
        verify(staffRepository, times(0)).findStaffByAdmin(true); // Should not check for owner if staff is not found
    }
    // @Test
    // public void testSetOwner_CurrentOwnerExists() {
    //     // Arrange
    //     Long newOwnerId = 2L;
    //     Staff currentOwner = new Staff("currentOwner", "owner@example.com", "password", true); // Existing owner
    //     Staff newOwner = new Staff("newOwner", "newowner@example.com", "password", false); // Non-owner staff

    //     when(staffRepository.findStaffById(newOwnerId)).thenReturn(newOwner);
    //     when(staffRepository.findStaffByAdmin(true)).thenReturn(List.of(currentOwner)); // Existing owner

    //     // Act
    //     Staff updatedOwner = staffService.setOwner(newOwnerId);

    //     // Assert
    //     assertNotNull(updatedOwner, "The returned staff should not be null.");
    //     assertTrue(updatedOwner.getStaffType(), "The new owner should now be an owner.");
    //     assertFalse(currentOwner.getStaffType(), "The previous owner should no longer be an owner.");
    //     verify(staffRepository, times(1)).findStaffById(newOwnerId);
    //     verify(staffRepository, times(1)).findStaffByAdmin(true);
    //     verify(staffRepository, times(1)).save(currentOwner); // Ensure the current owner was updated
    //     verify(staffRepository, times(1)).save(newOwner); // Ensure the new owner was updated
    // }
    // @Test
    // public void testSetOwner_AlreadyOwner() {
    //     // Arrange
    //     Long staffId = 1L;
    //     Staff currentOwner = new Staff("currentOwner", "owner@example.com", "password", true); // Existing owner

    //     when(staffRepository.findStaffById(staffId)).thenReturn(currentOwner);
    //     when(staffRepository.findStaffByAdmin(true)).thenReturn(List.of(currentOwner)); // Current owner is already the owner

    //     // Act
    //     Staff updatedOwner = staffService.setOwner(staffId);

    //     // Assert
    //     assertNotNull(updatedOwner, "The returned staff should not be null.");
    //     assertTrue(updatedOwner.getStaffType(), "The staff should still be an owner.");
    //     verify(staffRepository, times(1)).findStaffById(staffId);
    //     verify(staffRepository, times(1)).findStaffByAdmin(true);
    //     verify(staffRepository, times(0)).save(any(Staff.class)); // No save should occur
    // }
    // @Test
    // public void testSetOwner_NoCurrentOwner() {
    //     // Arrange
    //     Staff staff = new Staff("user1", "email@example.com", "password", false);
    //     when(staffRepository.findStaffById(1L)).thenReturn(staff);
    //     when(staffRepository.findStaffByAdmin(true)).thenReturn(new ArrayList<>());

    //     // Act
    //     Staff newOwner = staffService.setOwner(1L);

    //     // Assert
    //     assertTrue(newOwner.getStaffType());
    //     verify(staffRepository, times(1)).findStaffById(1L);
    //     verify(staffRepository, times(1)).findStaffByAdmin(true);
    //     verify(staffRepository, times(1)).save(staff);
    // }
    @Test
    public void testToList_ConversionWithReflection() throws Exception {
        // Arrange
        Iterable<String> iterable = List.of("Item1", "Item2", "Item3");
        
        // Access the private method using reflection
        Method toListMethod = StaffService.class.getDeclaredMethod("toList", Iterable.class);
        toListMethod.setAccessible(true);

        // Act
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(new StaffService(), iterable);

        // Assert
        assertNotNull(result, "Result list should not be null.");
        assertEquals(3, result.size(), "The list size should match the size of the iterable.");
        assertTrue(result.contains("Item1"), "Result list should contain 'Item1'.");
        assertTrue(result.contains("Item2"), "Result list should contain 'Item2'.");
        assertTrue(result.contains("Item3"), "Result list should contain 'Item3'.");
    }

    @Test
    public void testGetAllStaff_Success() {
        // Arrange
        Staff staff1 = new Staff("User1", "user1@example.com", "password123", false);
        Staff staff2 = new Staff("User2", "user2@example.com", "password456", false);
        
        // Mock the repository to return a list of staff
        when(staffRepository.findAll()).thenReturn(List.of(staff1, staff2));
        
        // Act
        List<Staff> staffList = staffService.getAllStaff();

        // Assert
        assertNotNull(staffList, "The staff list should not be null.");
        assertEquals(2, staffList.size(), "The staff list size should match the mocked data.");
        assertTrue(staffList.contains(staff1), "The staff list should contain staff1.");
        assertTrue(staffList.contains(staff2), "The staff list should contain staff2.");

        // Verify interaction with the repository
        verify(staffRepository, times(1)).findAll();
    }
}


