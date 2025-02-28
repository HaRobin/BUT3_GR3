package com.iut.banque.test.modele;

import com.iut.banque.modele.Compte;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestsCompte {

    private final String numeroCompte;
    private final boolean expectedResult;

    public TestsCompte(String numeroCompte, boolean expectedResult) {
        this.numeroCompte = numeroCompte;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                {"FR0123456789", true},  // Correct format
                {"F0123456789", false},  // Only one letter at start
                {"0123456789", false},   // No letters at start
                {"FRA0123456789", false}, // Three letters at start
                {"FR0123A456789", false}, // Letters in the middle
                {"FR00123456789", false}, // Too many digits
                {"FR123456789", false},  // Too few digits
                {"FR0123456789A", false} // Letters at the end
        });
    }

    @Test
    public void testCheckFormatNumeroCompte() {
        assertEquals("Failed for input: " + numeroCompte, expectedResult, Compte.checkFormatNumeroCompte(numeroCompte));
    }
}
