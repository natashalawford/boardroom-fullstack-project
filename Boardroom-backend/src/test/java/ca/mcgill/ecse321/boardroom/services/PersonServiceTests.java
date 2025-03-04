package ca.mcgill.ecse321.boardroom.services;

import ca.mcgill.ecse321.boardroom.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void testFindPerson() {
        //Arrange


        //Act

        //Assert

    }

    @Test
    public void testCreateValidPerson() {
        //Arrange


        //Act


        //Assert
    }

    @Test
    public void testCreateInvalidPerson() {}

    @Test
    public void testTogglePersonType() {

    }
}
