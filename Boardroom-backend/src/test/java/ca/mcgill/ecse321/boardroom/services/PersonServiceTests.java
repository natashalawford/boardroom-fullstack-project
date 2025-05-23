package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.PersonLoginDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockitoSettings
public class PersonServiceTests {
    @Mock
    private PersonRepository personRepo;
    @InjectMocks
    private PersonService personService;

    private static final String VALID_NAME = "John Doe";
    private static final String VALID_EMAIL = "john.doe@gmail.com";
    private static final String VALID_PASSWORD = "1234";
    private static final boolean VALID_OWNER = false;

    @Test
    public void testFindValidPerson() {
        //Arrange
        when(personRepo.findPersonById(1)).thenReturn(new Person(
                VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER));

        //Act
        Person foundPerson = personService.findPersonById(1);

        //Assert
        assertNotNull(foundPerson);
        assertEquals(VALID_NAME, foundPerson.getName());
        assertEquals(VALID_EMAIL, foundPerson.getEmail());
        assertEquals(VALID_PASSWORD, foundPerson.getPassword());
        assertEquals(VALID_OWNER, foundPerson.isOwner());

        verify(personRepo, times(1)).findPersonById(anyInt());
    }

    @Test
    public void testFindInvalidPerson() {
        //Act + Assert
        BoardroomException e = assertThrows(BoardroomException.class,
                () -> personService.findPersonById(2));

        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertEquals("No person has id 2", e.getMessage());

        verify(personRepo, times(1)).findPersonById(anyInt());
    }

    @Test
    public void testCreateValidPerson() {
        //Arrange
        PersonCreationDto personToCreate = new PersonCreationDto(VALID_NAME,
                VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);
        //Define behaviour for repo, when a person is saved, just return that
        // person
        when(personRepo.save(any(Person.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        //Act
        Person createdPerson = personService.createPerson(personToCreate);

        //Assert
        assertNotNull(createdPerson);

        assertEquals(personToCreate.getName(), createdPerson.getName());
        assertEquals(personToCreate.getEmail(), createdPerson.getEmail());
        assertEquals(personToCreate.getPassword(), createdPerson.getPassword());
        assertEquals(personToCreate.isOwner(), createdPerson.isOwner());

        //Make sure repo.save() is only called once
        verify(personRepo, times(1)).save(any(Person.class));
    }

    @Test
    public void testCreateInvalidPersonEmailAlreadyExists() {
        //Assert
        PersonCreationDto personToCreate = new PersonCreationDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);

        when(personRepo.existsByEmail(anyString())).thenReturn(true);

        //Act + Assert
        BoardroomException e = assertThrows(BoardroomException.class, () -> personService.createPerson(personToCreate));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

        //verify that save isn't being called
        verify(personRepo, times(0)).save(any(Person.class));
    }

    @Test
    public void testUpdateValidPerson() {
        //Arrange
        int id = 1;
        PersonRequestDto personToUpdate = new PersonRequestDto(VALID_NAME, VALID_EMAIL, VALID_OWNER);

        when(personRepo.findPersonById(id)).thenReturn(new Person(id, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER));
        when(personRepo.save(any(Person.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        //Act
        Person updatedPerson = personService.updatePerson(id, personToUpdate);

        //Assert
        assertNotNull(updatedPerson);  
        // assertEquals(1, updatedPerson.getId());
        assertEquals(VALID_NAME, updatedPerson.getName());
        assertEquals(VALID_EMAIL, updatedPerson.getEmail());
        assertEquals(VALID_OWNER, updatedPerson.isOwner());

        verify(personRepo, times(1)).findPersonById(anyInt());
        verify(personRepo, times(1)).save(any(Person.class));
    }

    @Test
    public void testDeleteValidPerson() {
        // Arrange
        int validId = 1;
        // Mock existing person
        Person existingPerson = new Person(validId, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);
        when(personRepo.findPersonById(validId)).thenReturn(existingPerson);

        // Act
        personService.deletePerson(validId);

        // Assert
        verify(personRepo, times(1)).findPersonById(validId);
        verify(personRepo, times(1)).delete(existingPerson);
    }
 
    @Test
    public void testValidLogin() {
        // Arrange
        Person existingPerson = new Person(1, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);
        when(personRepo.findByEmail(VALID_EMAIL)).thenReturn(existingPerson);

        PersonLoginDto loginDto = new PersonLoginDto(VALID_EMAIL, VALID_PASSWORD);

        // Act
        PersonResponseDto loggedInPerson = personService.login(loginDto);

        // Assert
        assertNotNull(loggedInPerson);
        assertEquals(VALID_NAME, loggedInPerson.getName());
        assertEquals(VALID_EMAIL, loggedInPerson.getEmail());

        verify(personRepo, times(1)).findByEmail(VALID_EMAIL);
    }

    @Test
    public void testInvalidLoginIncorrectEmail() {
        // Arrange
        PersonLoginDto loginDto = new PersonLoginDto("null@gmail.com", VALID_PASSWORD);

        // Act & Assert
        BoardroomException e = assertThrows(BoardroomException.class, () -> personService.login(loginDto));

        assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
        assertEquals("Invalid email or password", e.getMessage());

        verify(personRepo, times(1)).findByEmail("null@gmail.com");
    }

    @Test
    public void testInvalidLoginIncorrectPassword() {
        // Arrange
        Person existingPerson = new Person(1, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);
        when(personRepo.findByEmail(VALID_EMAIL)).thenReturn(existingPerson);

        PersonLoginDto loginDto = new PersonLoginDto(VALID_EMAIL, "wrongPassword");

        // Act & Assert
        BoardroomException e = assertThrows(BoardroomException.class, () -> personService.login(loginDto));

        assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
        assertEquals("Invalid email or password", e.getMessage());

        verify(personRepo, times(1)).findByEmail(VALID_EMAIL);
    }
}
