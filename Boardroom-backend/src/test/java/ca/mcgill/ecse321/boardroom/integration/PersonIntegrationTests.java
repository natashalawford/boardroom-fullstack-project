package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
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
    @Order(2)
    public void testUpdateValidPerson() {
        //Arrange
        PersonRequestDto updatePerson = new PersonRequestDto(VALID_NAME, VALID_EMAIL, VALID_OWNER);
        String url = "/people/{id}/role";


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

    @Test
    @Order(1)
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
}
