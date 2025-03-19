package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.boardroom.dtos.ErrorDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonLoginDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.creation.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import ca.mcgill.ecse321.boardroom.services.PersonService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepo;

    private static final String VALID_NAME = "John Doe";
    private static final String VALID_EMAIL = "john.doe@gmail.com";
    private static final String VALID_PASSWORD = "1234";
    private static final boolean VALID_OWNER = false;

    private static int createdPersonId;

    @AfterAll
    public void resetDatabase() {
        personRepo.deleteAll();
    }

    @Test
    @SuppressWarnings("null")
    @Order(0)
    public void testCreateValidPerson() {
        //Arrange
        PersonCreationDto personToCreate = new PersonCreationDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);
        String url = "/people";

        //Act
        ResponseEntity<PersonResponseDto> response = client.postForEntity(url, personToCreate, PersonResponseDto.class);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());

        //Get person from db and check password
        assertNotNull(personService.findPersonById(response.getBody().getId()));
        assertEquals(VALID_PASSWORD, personService.findPersonById(response.getBody().getId()).getPassword());

        createdPersonId = response.getBody().getId();
    }

    @Test
    @SuppressWarnings("null")
    @Order(1)
    public void testCreateInvalidPersonEmailExistsAlready() {
        //Arrange
        String url = "/people";
        PersonCreationDto existingPerson = new PersonCreationDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);

        //Act
        ResponseEntity<ErrorDto> response = client.postForEntity(url, existingPerson, ErrorDto.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This email is already in use", response.getBody().getErrors().get(0));

    }

    @Test
    @SuppressWarnings("null")
    @Order(2)
    public void testGetValidPerson() {
        //Arrange
        String url = "/people/{id}";

        //Act
        ResponseEntity<PersonResponseDto> response = client.getForEntity(url, PersonResponseDto.class, createdPersonId);

        //Assert
        assertNotNull(response);

        assertEquals(HttpStatus.OK, response.getStatusCode()); 
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_OWNER, response.getBody().isOwner());
    }

    @Test
    @SuppressWarnings("null")
    @Order(3)
    public void testGetInvalidPerson() {
        //Arrange
        String url = "/people/{id}";

        //Act 
        ResponseEntity<ErrorDto> response = client.getForEntity(url, ErrorDto.class, -1);

        //Assert
        assertNotNull(response);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No person has id -1", response.getBody().getErrors().get(0));
    }


    @Test
    @SuppressWarnings("null")
    @Order(4)
    public void testUpdateValidPerson() {
        //Arrange
        PersonRequestDto updatePerson = new PersonRequestDto(VALID_NAME, VALID_EMAIL, VALID_OWNER);
        String url = "/people/{id}";


        //Act
        ResponseEntity<PersonResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<PersonRequestDto>(updatePerson), PersonResponseDto.class, createdPersonId);

        //Asert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdPersonId, response.getBody().getId());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_OWNER, response.getBody().isOwner());
    }

    //update person that doesn't exist
    @Test
    @SuppressWarnings("null")
    @Order(5)
    public void testUpdateInvalidPerson() {
        //Arrange
        String url = "/people/{id}";
        PersonRequestDto updatePerson = new PersonRequestDto(VALID_NAME, VALID_EMAIL, VALID_OWNER);


        //Act
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<PersonRequestDto>(updatePerson), ErrorDto.class, -1);

        //Assert
        assertNotNull(response);
        assertEquals("No person has id -1", response.getBody().getErrors().get(0));

    }


    @Test
    @SuppressWarnings("null")
    @Order(6)
    public void testLoginValidPerson() {
        // Arrange
        String url = "/people/login";
        PersonLoginDto loginDto = new PersonLoginDto(VALID_EMAIL, VALID_PASSWORD);
    
        // Act
        ResponseEntity<PersonResponseDto> response =
            client.postForEntity(url, loginDto, PersonResponseDto.class);
    
        // Assert
        assertNotNull(response.getBody()); 

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(createdPersonId, response.getBody().getId());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_OWNER, response.getBody().isOwner());
        
    }


    @Test
    @SuppressWarnings("null")
    @Order(7)
    public void testLoginInvalidEmail() {
        // Arrange
        String url = "/people/login";  
        PersonLoginDto loginDto = new PersonLoginDto("invalidemail@gmail.com", VALID_PASSWORD);

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity(url, loginDto, ErrorDto.class);

        // Assert
        assertNotNull(response);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password", response.getBody().getErrors().get(0));
    }

    @Test
    @SuppressWarnings("null")
    @Order(8)
    public void testLoginFailIncorrectPassword() {
        // Arrange
        String url = "/people/login"; 
        PersonLoginDto loginDto = new PersonLoginDto(VALID_EMAIL, "wrongPassword");

        // Act
        ResponseEntity<ErrorDto> response = client.postForEntity(url, loginDto, ErrorDto.class);

        // Assert
        assertNotNull(response);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());;
        assertEquals("Invalid email or password", response.getBody().getErrors().get(0));
    } 

    @Test
    @Order(9)
    public void testDeleteValidPerson() {
        //Arrange
        String url = "/people/{id}";

        //Act
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class, createdPersonId);

        //Assert
        assertNotNull(response);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        //Check with repo to make sure it doesn't exist
        assertNull(personRepo.findPersonById(createdPersonId));

    }

    @Test
    @SuppressWarnings("null")
    @Order(10)
    public void testDeleteInvalidPerson() {
        //Arrange
        String url = "/people/{id}";

        //Act
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.DELETE, null, ErrorDto.class, createdPersonId);

        //Assert
        assertNotNull(response);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("No person has id %d", createdPersonId), response.getBody().getErrors().get(0));
    }
}
