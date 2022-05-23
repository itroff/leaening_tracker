package tracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void checkSpecCharacters() {
        assertEquals(false, Main.checkSpecCharacters("a'-a"));
        assertEquals(false, Main.checkSpecCharacters("adsad-'dfgdf"));
        assertEquals(true, Main.checkSpecCharacters("adsad-dfgdf"));
        assertEquals(true, Main.checkSpecCharacters("a'dfgdf"));
    }

    @Test
    void validateName() {
        assertEquals(true, Main.validateName("John"));
        assertEquals(true, Main.validateName("Jane"));
        assertEquals(false, Main.validateName("J."));
        assertEquals(true, Main.validateName("Jean-Clause"));
        assertEquals(false, Main.validateName("陳"));
    }

    @Test
    void validateSurname() {
        assertEquals(true, Main.validateSurname("Doe"));
        assertEquals(false, Main.validateSurname("D."));
        assertEquals(true, Main.validateSurname("van Helsing"));
        assertEquals(true, Main.validateSurname("Luise Johnson"));
    }

    @Test
    void checkEmail() {
        assertEquals(true, Main.checkEmail("name@domain.com"));
        assertEquals(false, Main.checkEmail("email"));
        assertEquals(true, Main.checkEmail("jc@google.it"));
        assertEquals(true, Main.checkEmail("maryj@google.com"));
        assertEquals(false, Main.checkEmail("maryj@googlecom"));
    }
}