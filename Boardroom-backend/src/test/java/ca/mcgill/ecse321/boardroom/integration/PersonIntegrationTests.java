package ca.mcgill.ecse321.boardroom.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.http.HttpHeaders;

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
import ca.mcgill.ecse321.boardroom.dtos.PersonCreationDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonLoginDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonRequestDto;
import ca.mcgill.ecse321.boardroom.dtos.PersonUpdatePasswordDto;
import ca.mcgill.ecse321.boardroom.dtos.responses.PersonResponseDto;
import ca.mcgill.ecse321.boardroom.exceptions.BoardroomException;
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
        // Arrange
        PersonCreationDto personToCreate = new PersonCreationDto(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_OWNER);
        String url = "/people";

        // Act
        ResponseEntity<PersonResponseDto> response = client.postForEntity(url, personToCreate, PersonResponseDto.class);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());

        // Get person from db and check password
        assertNotNull(personService.findPersonById(response.getBody().getId()));
        assertEquals(VALID_PASSWORD, personService.findPersonById(response.getBody().getId()).getPassword());

        createdPersonId = response.getBody().getId();
    }

    @Test
    @Order(2)
    public void testUpdateValidPerson() {
        // Arrange
        PersonRequestDto updatePerson = new PersonRequestDto(VALID_NAME, VALID_EMAIL, VALID_OWNER);
        String url = "/people/{id}/role";

        // Act
        ResponseEntity<PersonResponseDto> response = client.exchange(url, HttpMethod.PUT,
                new HttpEntity<PersonRequestDto>(updatePerson), PersonResponseDto.class, createdPersonId);

        // Asert
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
        // Arrange
        String url = "/people/{id}";

        // Act
        ResponseEntity<PersonResponseDto> response = client.getForEntity(url, PersonResponseDto.class, createdPersonId);

        // Assert
        assertNotNull(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_OWNER, response.getBody().isOwner());
    }

    @Order(3)
    public void testLoginValidPerson() {
        // Arrange
        String url = "/people/login";
        PersonLoginDto loginDto = new PersonLoginDto(VALID_EMAIL, VALID_PASSWORD);

        // Act
        // Send the PersonLoginDto as JSON via POST
        ResponseEntity<PersonResponseDto> response = client.postForEntity(url, loginDto, PersonResponseDto.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null for a valid login.");

        // If your Person object has an 'id' field, ensure it's the one you expect
        assertEquals(createdPersonId, response.getBody().getId());
        assertEquals(VALID_NAME, response.getBody().getName());
        assertEquals(VALID_EMAIL, response.getBody().getEmail());
        assertEquals(VALID_OWNER, response.getBody().isOwner());

    }

    @Test
    @Order(4)
    public void testLoginInvalidEmail() {
        // Arrange
        String url = "/people/login";
        String invalidEmail = "dne@example.com";

        // Email does not exist in DB
        PersonLoginDto loginDto = new PersonLoginDto(invalidEmail, "SomePassword");

        // Act
        // response as String so we can parse the error message
        ResponseEntity<String> response = client.postForEntity(url, loginDto, String.class);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Should return 401 if the email does not exist.");
        String responseBody = response.getBody();
        assertNotNull(responseBody, "Response body must not be null for an error.");
        assertTrue(
                responseBody.contains("Invalid email or password"),
                "Expected error message to contain 'Invalid email or password'.");
    }

    @Test
    @Order(5)
    public void testLoginFailIncorrectPassword() {
        // Arrange
        String url = "/people/login";

        // Valid email, but wrong password
        PersonLoginDto loginDto = new PersonLoginDto(VALID_EMAIL, "wrongPassword");

        // Act
        ResponseEntity<String> response = client.postForEntity(url, loginDto, String.class);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Should return 401 if the password is incorrect.");
        String responseBody = response.getBody();
        assertNotNull(responseBody, "Response body must not be null on error.");
        assertTrue(
                responseBody.contains("Invalid email or password"),
                "Expected error message to contain 'Invalid email or password'.");
    }

    @Test
    @Order(6)
    public void testLoginFailPasswordMissing() {
        // Arrange
        String url = "/people/login";

        // Null password => triggers BAD_REQUEST
        PersonLoginDto loginDto = new PersonLoginDto(VALID_EMAIL, null);

        // Act
        ResponseEntity<String> response = client.postForEntity(url, loginDto, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Should return 400 if email or password is missing.");
        String responseBody = response.getBody();
        assertNotNull(responseBody, "Response body must not be null on error.");
        assertTrue(
                responseBody.contains("Email and password are required."),
                "Expected error message to contain 'Email and password are required.'.");
    }

    // delete person by id tests begin here
    @Test
    @Order(13)
    public void testDeleteValidPerson() {
        // Arrange
        String url = "/people/" + createdPersonId;

        // Act
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Optionally, confirm that the person is gone by calling the service directly
        BoardroomException ex = assertThrows(BoardroomException.class,
                () -> personService.findPersonById(createdPersonId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertTrue(ex.getMessage().contains("No person has id " + createdPersonId));
    }

    @Test
    @Order(7)
    public void testDeleteInvalidPerson() {
        // Arrange
        int nonExistentId = 9999;
        String url = "/people/" + nonExistentId;

        // Act
        ResponseEntity<String> response = client.exchange(url, HttpMethod.DELETE, null, String.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // You can also check the error message returned:
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("No person has id " + nonExistentId));
    }

    @Test
    @Order(8)
    public void testChangePasswordValid() {
        // Arrange
        String url = "/people/{id}/password";
        PersonUpdatePasswordDto passwordDto = new PersonUpdatePasswordDto(VALID_PASSWORD, "newPassword123");

        HttpEntity<PersonUpdatePasswordDto> request = new HttpEntity<>(passwordDto);

        // Act
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.PUT, request, Void.class, createdPersonId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Password change should succeed.");
        assertEquals(passwordDto.getNewPassword(), personService.findPersonById(createdPersonId).getPassword(),
                "Password should be updated.");
    }

    @Test
    @Order(9)
    public void testChangePasswordInvalidId() {
        // Arrange
        int nonExistentId = 9999;
        String url = "/people/{id}/password";
        PersonUpdatePasswordDto passwordDto = new PersonUpdatePasswordDto("1234", "newPassword123"); // Assuming "1234"
                                                                                                     // is the old
                                                                                                     // password
        HttpEntity<PersonUpdatePasswordDto> request = new HttpEntity<>(passwordDto);

        // Act
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, request, ErrorDto.class, nonExistentId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No person has id " + nonExistentId, response.getBody().getErrors().get(0).replace("[", "").replace("]", ""));
        
    }

    @Test
    @Order(10)
    public void testChangePasswordInvalidPassword() {
        // Arrange
        String url = "/people/{id}/password";
        PersonUpdatePasswordDto passwordDto = new PersonUpdatePasswordDto("wrongOldPassword", "newPassword123"); 
        HttpEntity<PersonUpdatePasswordDto> request = new HttpEntity<>(passwordDto);

        // Act
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, request, ErrorDto.class, createdPersonId);
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid password", response.getBody().getErrors().get(0).replace("[", "").replace("]", ""));
    }

    @Test
    @Order(11)
    public void testChangePasswordEmptyNewPassword() {
        // Arrange
        String url = "/people/{id}/password";
        PersonUpdatePasswordDto passwordDto = new PersonUpdatePasswordDto("1234", ""); // Empty new password
        HttpEntity<PersonUpdatePasswordDto> request = new HttpEntity<>(passwordDto);

        // Act
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, request, ErrorDto.class, createdPersonId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password is required", response.getBody().getErrors().get(0).replace("[", "").replace("]", ""));
    }

    @Test
    @Order(12)
    public void testChangePasswordNullNewPassword() {
        // Arrange
        String url = "/people/{id}/password";
        PersonUpdatePasswordDto passwordDto = new PersonUpdatePasswordDto("1234", null); // Null new password
        HttpEntity<PersonUpdatePasswordDto> request = new HttpEntity<>(passwordDto);

        // Act
        ResponseEntity<ErrorDto> response = client.exchange(url, HttpMethod.PUT, request, ErrorDto.class, createdPersonId);
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password is required", response.getBody().getErrors().get(0).replace("[", "").replace("]", ""));
    }

}