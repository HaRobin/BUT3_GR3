package com.iut.banque.test.utils;

import com.iut.banque.utils.PasswordUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPasswordUtils {
    @Test
    public void testHashPwd() {
        // Test pour vérifier le hachage du mot de passe
        String password = "myPassword123";
        String hashedPassword = PasswordUtils.hashPwd(password);

        // Vérifier que le hachage n'est pas null
        assertNotNull("The hashed password should not be null", hashedPassword);

        // Vérifier que le hachage a la bonne longueur (SHA-256 -> 64 characters)
        assertEquals("The hashed password should have 64 characters", 64, hashedPassword.length());
    }

    @Test
    public void testHashPwdConsistency() {
        // Vérifier que le hachage d'un mdp est consistant
        String password = "myPassword123";
        String hashedPassword1 = PasswordUtils.hashPwd(password);
        String hashedPassword2 = PasswordUtils.hashPwd(password);

        // Vérifier que 2 hachages du même mdp donne le même hachage
        assertEquals("The hashed password should be consistent", hashedPassword1, hashedPassword2);
    }

    @Test
    public void testDifferentPasswordsHashDifferently() {
        // Vérifier que des mots de passe différents génèrent des hachages différents
        String password1 = "password1";
        String password2 = "password2";

        String hashedPassword1 = PasswordUtils.hashPwd(password1);
        String hashedPassword2 = PasswordUtils.hashPwd(password2);

        // Vérifier que les deux hachages sont différents
        assertNotEquals("Different passwords should generate different hashes", hashedPassword1, hashedPassword2);
    }
}
