package com.iut.banque.test.modele;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;
import com.iut.banque.modele.CompteAvecDecouvert;
import com.iut.banque.modele.CompteSansDecouvert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestsClient {

    private Client client;

    @Before
    public void setUp() throws IllegalFormatException, IllegalArgumentException {
        client = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password", "1234567890");
    }

    @Test
    public void testMethodeCheckFormatUserIdClientCorrect() {
        assertTrue(Client.checkFormatUserIdClient("a.utilisateur928"));
    }

    @Test
    public void testMethodeCheckFormatUserIdClientIncorrect() {
        assertFalse(Client.checkFormatUserIdClient("32a.abc1"));
        assertFalse(Client.checkFormatUserIdClient("aaa.abc1"));
        assertFalse(Client.checkFormatUserIdClient("abc1"));
        assertFalse(Client.checkFormatUserIdClient(""));
        assertFalse(Client.checkFormatUserIdClient("a.138"));
        assertFalse(Client.checkFormatUserIdClient("a.bcdé1"));
        assertFalse(Client.checkFormatUserIdClient("a.abc01"));
        assertFalse(Client.checkFormatUserIdClient("a.ab.c1"));
    }

    @Test
    public void testMethodeCheckFormatUserIdClientAvecUneSeuleLettreApresLePointSeparateur() {
        assertTrue(Client.checkFormatUserIdClient("a.a1"));
    }

    @Test
    public void testMethodeCheckFormatNumeroClientCorrect() {
        assertTrue(Client.checkFormatNumeroClient("1234567890"));
    }

    @Test
    public void testMethodeCheckFormatNumeroClientIncorrect() {
        assertFalse(Client.checkFormatNumeroClient("12a456789"));
        assertFalse(Client.checkFormatNumeroClient("12#456789"));
        assertFalse(Client.checkFormatNumeroClient("12345678"));
        assertFalse(Client.checkFormatNumeroClient("12345678901"));
    }

    @Test
    public void testPossedeComptesADecouvert_AvecComptesSansDecouvert() throws IllegalFormatException {
        client.addAccount(new CompteSansDecouvert("FR1234567890", 42, client));
        client.addAccount(new CompteSansDecouvert("FR1234567891", 0, client));

        assertFalse(client.possedeComptesADecouvert());
    }

    @Test
    public void testPossedeComptesADecouvert_SansCompte() {
        assertFalse(client.possedeComptesADecouvert());
    }

    @Test
    public void testPossedeComptesADecouvert_AvecUnCompteADecouvert() throws IllegalFormatException, IllegalOperationException {
        client.addAccount(new CompteSansDecouvert("FR1234567890", 42, client));
        client.addAccount(new CompteAvecDecouvert("FR1234567892", -42, 100, client));

        assertTrue(client.possedeComptesADecouvert());
    }

    @Test
    public void testPossedeComptesADecouvert_AvecPlusieursComptesADecouvert() throws IllegalFormatException, IllegalOperationException {
        client.addAccount(new CompteAvecDecouvert("FR1234567892", -42, 100, client));
        client.addAccount(new CompteAvecDecouvert("FR1234567893", -4242, 5000, client));

        assertTrue(client.possedeComptesADecouvert());
    }

    @Test
    public void testGetComptesAvecSoldeNonNul_AvecDeuxComptesSoldeNul() throws IllegalFormatException, IllegalOperationException {
        client.addAccount(new CompteAvecDecouvert("FR1234567890", 0, 42, client));
        client.addAccount(new CompteSansDecouvert("FR1234567891", 0, client));

        assertTrue(client.getComptesAvecSoldeNonNul().isEmpty());
    }

    @Test
    public void testGetComptesAvecSoldeNonNul_AvecUnCompteAvecSoldeNonNul() throws IllegalFormatException {
        client.addAccount(new CompteSansDecouvert("FR1234567891", 1, client));

        assertNotNull(client.getComptesAvecSoldeNonNul().get("FR1234567891"));
    }

    @Test
    public void testToStringWithRegex() {
        String result = client.toString();
        assertTrue(result.matches(".*userId=j\\.doe1.*nom=John.*prenom=Doe.*adresse=20 rue Bouvier.*male=true.*numeroClient=1234567890.*"));
    }

    @Test
    public void testSetNumeroClient_CorrectFormat() throws IllegalFormatException {
        client.setNumeroClient("0987654321");
        assertEquals("0987654321", client.getNumeroClient());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeroClient_NullValue() throws IllegalFormatException {
        client.setNumeroClient(null);
    }

    @Test(expected = IllegalFormatException.class)
    public void testSetNumeroClient_IncorrectFormat() throws IllegalFormatException {
        client.setNumeroClient("12a456789");
    }

    @Test
    public void testSetUserId_CorrectFormat() throws IllegalFormatException {
        client.setUserId("b.user123");
        assertEquals("b.user123", client.getUserId());
    }

    @Test(expected = IllegalFormatException.class)
    public void testSetUserId_IncorrectFormat() throws IllegalFormatException {
        client.setUserId("user!23");
    }

    @Test
    public void testGetIdentity() {
        assertEquals("Doe John (1234567890)", client.getIdentity());
    }

    @Test
    public void testSetAccounts() throws IllegalFormatException, IllegalOperationException{
        Map<String, Compte> comptes = new HashMap<>();
        comptes.put("FR1234567890", new CompteSansDecouvert("FR1234567890", 100, client));
        comptes.put("FR0987654321", new CompteAvecDecouvert("FR0987654321", -50, 200, client));

        client.setAccounts(comptes);

        assertEquals(2, client.getAccounts().size());
        assertTrue(client.getAccounts().containsKey("FR1234567890"));
        assertTrue(client.getAccounts().containsKey("FR0987654321"));
    }
}
