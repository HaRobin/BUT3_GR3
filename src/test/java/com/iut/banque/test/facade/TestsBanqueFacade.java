package com.iut.banque.test.facade;

import com.iut.banque.constants.LoginConstants;
import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.InsufficientFundsException;
import com.iut.banque.exceptions.TechnicalException;
import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.facade.BanqueManager;
import com.iut.banque.facade.LoginManager;
import com.iut.banque.modele.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class TestsBanqueFacade {
    private BanqueFacade banqueFacade;
    private LoginManager loginMng;
    private BanqueManager banqueMng;


    @Before
    public void setUp() {
        loginMng = Mockito.mock(LoginManager.class);
        banqueMng = Mockito.mock(BanqueManager.class);
        banqueFacade = new BanqueFacade(loginMng, banqueMng);
    }

    @Test
    public void testGetConnectedUser() {
        Utilisateur u = Mockito.mock(Utilisateur.class);
        Mockito.when(loginMng.getConnectedUser()).thenReturn(u);
        Assert.assertEquals(u, banqueFacade.getConnectedUser());
    }

    @Test
    public void testTryLoginManagerIsNotConnected() {
        Mockito.when(loginMng.tryLogin(Mockito.anyString(), Mockito.anyString())).thenReturn(LoginConstants.USER_IS_CONNECTED);
        Assert.assertEquals(LoginConstants.USER_IS_CONNECTED, banqueFacade.tryLogin("test", "test"));
    }

    @Test
    public void testTryLoginManagerIsConnected() {
        Mockito.when(loginMng.tryLogin(Mockito.anyString(), Mockito.anyString())).thenReturn(LoginConstants.MANAGER_IS_CONNECTED);
        Assert.assertEquals(LoginConstants.MANAGER_IS_CONNECTED, banqueFacade.tryLogin("test", "test"));
        Mockito.verify(banqueMng, Mockito.times(1)).loadAllClients();
    }

    @Test
    public void testCrediter() throws IllegalFormatException {
        Compte c = Mockito.mock(Compte.class);
        banqueFacade.crediter(c, 0);
        Mockito.verify(banqueMng, Mockito.times(1)).crediter(c, 0);
    }

    @Test
    public void testDebiter() throws IllegalFormatException, InsufficientFundsException {
        Compte c = Mockito.mock(Compte.class);
        banqueFacade.debiter(c, 0);
        Mockito.verify(banqueMng, Mockito.times(1)).debiter(c, 0);
    }

    @Test
    public void testGetAllClients() {
        Map<String, Client> m = new HashMap<>();
        Mockito.when(banqueMng.getAllClients()).thenReturn(m);
        Assert.assertEquals(m, banqueFacade.getAllClients());
        Mockito.verify(banqueMng, Mockito.times(1)).getAllClients();
    }

    @Test
    public void testLogout() {
        banqueFacade.logout();
        Mockito.verify(loginMng, Mockito.times(1)).logout();
    }

    @Test
    public void testCreateAccountConnectedUserIsNotGestionnaire() throws TechnicalException, IllegalFormatException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        Client c = Mockito.mock(Client.class);
        banqueFacade.createAccount("asd", c);
        Mockito.verify(banqueMng, Mockito.times(0)).createAccount(Mockito.anyString(), Mockito.any(Client.class));
    }

    @Test
    public void testCreateAccountConnectedUserIsGestionnaire() throws TechnicalException, IllegalFormatException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        Client c = Mockito.mock(Client.class);
        banqueFacade.createAccount("asd", c);
        Mockito.verify(banqueMng, Mockito.times(1)).createAccount("asd", c);
    }

    @Test
    public void testCreateAccountWithDecouvertAutoriseConnectedUserIsNotGestionnaire() throws TechnicalException, IllegalFormatException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        Client c = Mockito.mock(Client.class);
        banqueFacade.createAccount("asd", c, 0);
        Mockito.verify(banqueMng, Mockito.times(0)).createAccount(Mockito.anyString(), Mockito.any(Client.class), Mockito.anyDouble());
    }

    @Test
    public void testCreateAccountWithDecouvertAutoriseConnectedUserIsGestionnaire() throws TechnicalException, IllegalFormatException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        Client c = Mockito.mock(Client.class);
        banqueFacade.createAccount("asd", c, 0);
        Mockito.verify(banqueMng, Mockito.times(1)).createAccount("asd", c, 0);
    }

    @Test
    public void testDeleteAccountConnectedUserIsNotGestionnaire() throws TechnicalException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        banqueFacade.deleteAccount(Mockito.mock(Compte.class));
        Mockito.verify(banqueMng, Mockito.times(0)).deleteAccount(Mockito.any(Compte.class));
    }

    @Test
    public void testDeleteAccountConnectedUserIsGestionnaire() throws TechnicalException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        Compte c = Mockito.mock(Compte.class);
        banqueFacade.deleteAccount(c);
        Mockito.verify(banqueMng, Mockito.times(1)).deleteAccount(c);
    }

    @Test
    public void testCreateManagerConnectedUserIsNotGestionnaire() throws TechnicalException, IllegalFormatException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        banqueFacade.createManager("test", "test", "test", "test", "test", true);
        Mockito.verify(banqueMng, Mockito.times(0)).createManager(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
    }

    @Test
    public void testCreateManagerConnectedUserIsGestionnaire() throws TechnicalException, IllegalFormatException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        banqueFacade.createManager("test", "test", "test", "test", "test", true);
        Mockito.verify(banqueMng, Mockito.times(1)).createManager("test", "test", "test", "test", "test", true);
    }

    @Test
    public void testCreateClientConnectedUserIsNotGestionnaire() throws TechnicalException, IllegalFormatException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        banqueFacade.createClient("test", "test", "test", "test", "test", true, "test");
        Mockito.verify(banqueMng, Mockito.times(0)).createClient(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString());
    }

    @Test
    public void testCreateClientConnectedUserIsGestionnaire() throws TechnicalException, IllegalFormatException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        banqueFacade.createClient("test", "test", "test", "test", "test", true, "test");
        Mockito.verify(banqueMng, Mockito.times(1)).createClient("test", "test", "test", "test", "test", true, "test");
    }

    @Test
    public void testDeleteUserConnectedUserIsNotGestionnaire() throws TechnicalException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        banqueFacade.deleteUser(Mockito.mock(Utilisateur.class));
        Mockito.verify(banqueMng, Mockito.times(0)).deleteUser(Mockito.any(Utilisateur.class));
    }

    @Test
    public void testDeleteUserConnectedUserIsGestionnaire() throws TechnicalException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        Utilisateur u = Mockito.mock(Utilisateur.class);
        banqueFacade.deleteUser(u);
        Mockito.verify(banqueMng, Mockito.times(1)).deleteUser(u);
    }

    @Test
    public void testLoadClientsConnectedUserIsNotGestionnaire() throws TechnicalException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        banqueFacade.loadClients();
        Mockito.verify(banqueMng, Mockito.times(0)).loadAllClients();
    }

    @Test
    public void testLoadClientsConnectedUserIsGestionnaire() throws TechnicalException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        banqueFacade.loadClients();
        Mockito.verify(banqueMng, Mockito.times(1)).loadAllClients();
    }

    @Test
    public void testGetCompte() {
        Compte c = Mockito.mock(Compte.class);
        Mockito.when(banqueMng.getAccountById(Mockito.anyString())).thenReturn(c);
        Assert.assertEquals(c, banqueFacade.getCompte("test"));
        Mockito.verify(banqueMng, Mockito.times(1)).getAccountById("test");
    }

    @Test
    public void testChangeDecouvertConnectedUserIsNotGestionnaire() throws TechnicalException, IllegalFormatException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Utilisateur.class));
        banqueFacade.changeDecouvert(Mockito.mock(CompteAvecDecouvert.class), 0);
        Mockito.verify(banqueMng, Mockito.times(0)).changeDecouvert(Mockito.any(CompteAvecDecouvert.class), Mockito.anyDouble());
    }

    @Test
    public void testChangeDecouvertConnectedUserIsGestionnaire() throws TechnicalException, IllegalFormatException, IllegalOperationException {
        Mockito.when(loginMng.getConnectedUser()).thenReturn(Mockito.mock(Gestionnaire.class));
        CompteAvecDecouvert c = Mockito.mock(CompteAvecDecouvert.class);
        banqueFacade.changeDecouvert(c, 0);
        Mockito.verify(banqueMng, Mockito.times(1)).changeDecouvert(c, 0);
    }
}
