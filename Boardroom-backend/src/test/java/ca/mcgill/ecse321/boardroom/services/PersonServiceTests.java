package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonUpdateDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
import ca.mcgill.ecse321.boardroom.model.Person;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        when(personRepo.findPersonById(1)).thenReturn(new Person(1,
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
    public void testUpdateValidPerson() {
        //Arrange
        PersonUpdateDto personToUpdate = new PersonUpdateDto(1, VALID_NAME,
                VALID_EMAIL,
                VALID_PASSWORD, VALID_OWNER);

        when(personRepo.existsById(1)).thenReturn(true);
        when(personRepo.save(any(Person.class))).thenAnswer((InvocationOnMock iom) -> iom.getArgument(0));

        //Act
        Person updatedPerson = personService.updatePerson(personToUpdate);

        //Assert
        assertNotNull(updatedPerson);
        assertEquals(VALID_OWNER, updatedPerson.isOwner());

        assertEquals(1, updatedPerson.getId());
        assertEquals(VALID_NAME, updatedPerson.getName());
        assertEquals(VALID_EMAIL, updatedPerson.getEmail());
        assertEquals(VALID_PASSWORD, updatedPerson.getPassword());

        verify(personRepo, times(1)).existsById(anyInt());
        verify(personRepo, times(1)).save(any(Person.class));
    }

    @Test
    public void testUpdateInvalidPerson() {
        //Arrange
        PersonUpdateDto personToUpdate = new PersonUpdateDto(2, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);

        //Act + Assert
        BoardroomException e = assertThrows(BoardroomException.class, () -> personService.updatePerson(personToUpdate));
        
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertEquals("A person with this id does not exist", e.getMessage());
    }
}
