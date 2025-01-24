package com.iut.banque.test.modele;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.InsufficientFundsException;
import com.iut.banque.exceptions.TechnicalException;
import com.iut.banque.modele.Banque;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;
import com.iut.banque.modele.CompteAvecDecouvert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestBanque {

    private Banque banque;

    @Mock
    private Compte mockCompte;

    @Mock
    Compte spyCompte;

    @Mock
    private CompteAvecDecouvert mockCompteAvecDecouvert;

    @Before
    public void setUp() throws TechnicalException, IllegalFormatException {
        MockitoAnnotations.openMocks(this);
        banque = new Banque();
        mockCompte = mock(Compte.class);

        // Configuration tests
        Map<String, Client> clients = new HashMap<>();
        clients.put("client1", new Client("Lidell", "Alice", "789, grande rue, Metz", true , "a.lidell1",  "31f7a65e315586ac198bd798b6629ce4903d0899476d5741a9f32e2e521b6a66", "9865432100"));
        banque.setClients(clients);

        Map<String, Compte> comptes = new HashMap<>();
        comptes.put("compte1", mockCompte);
        banque.setAccounts(comptes);
    }

    @Test
    public void testGetClients() {
        Map<String, Client> clients = banque.getClients();
        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    @Test
    public void testDebiterCompteAvecSoldeSuffisant() throws InsufficientFundsException, IllegalFormatException { // il faut faire en sorte que 1500 euros soit vraiment sur le compte sans utiliser when et thenreturn >>>> UTILISER SPY

        spyCompte = Mockito.spy(new CompteAvecDecouvert());

        spyCompte.crediter(1500.0);

        banque.debiter(spyCompte, 500.0);

        assertEquals(1000.0, spyCompte.getSolde(), 0.001);

    }

    @Test
    public void testDebiterExceptionInsufficientFunds() throws InsufficientFundsException, IllegalFormatException {
        doThrow(new InsufficientFundsException("Solde insuffisant")).when(mockCompte).debiter(1500.0);

        try {
            banque.debiter(mockCompte, 1500.0);
            assertTrue("L'exception InsufficientFundsException n'a pas été lancée !", false);
        } catch (InsufficientFundsException e) {
            assertTrue(e instanceof InsufficientFundsException);
        }

        // verifie que la méthode debiter a été appelée une fois
        verify(mockCompte, times(1)).debiter(1500.0);
    }

    @Test
    public void testCrediter() throws IllegalFormatException {

        spyCompte = Mockito.spy(new CompteAvecDecouvert());

        banque.crediter(spyCompte, 200.0);

        assertEquals(200.0, spyCompte.getSolde(), 0.001);
    }

    @Test
    public void testDeleteUser() {
        banque.deleteUser("client1");
        assertFalse(banque.getClients().containsKey("client1"));
    }

    @Test
    public void testChangeDecouvert() throws IllegalFormatException, IllegalOperationException {
        doNothing().when(mockCompteAvecDecouvert).setDecouverAutorise(500.0);

        banque.changeDecouvert(mockCompteAvecDecouvert, 500.0);

        verify(mockCompteAvecDecouvert, times(1)).setDecouverAutorise(500.0);
    }

    @Test
    public void testChangeDecouvertException() throws IllegalFormatException, IllegalOperationException {
        doThrow(new IllegalOperationException("Opération non autorisée")).when(mockCompteAvecDecouvert).setDecouverAutorise(-500.0);

        try {
            banque.changeDecouvert(mockCompteAvecDecouvert, -500.0);
            assertTrue("L'exception IllegalOperationException n'a pas été lancée !", false);
        } catch (IllegalOperationException e) {
            assertTrue(e instanceof IllegalOperationException);
        }

        // verifie que la méthode setDecouverAutorise a été appelée une fois
        verify(mockCompteAvecDecouvert, times(1)).setDecouverAutorise(-500.0);
    }
}
