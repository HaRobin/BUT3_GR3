package com.iut.banque.test.modele;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.InsufficientFundsException;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;
import com.iut.banque.modele.CompteSansDecouvert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests en rapport avec la méthode "créditer" de la classe Banque
 */
public class TestsCompte {

    private Compte compte;
    private Client client;

    @Before
    public void setUp() throws IllegalFormatException {
        client = new Client();
        compte = new CompteSansDecouvert("AB7328887341", 0, client);
    }

    /**
     * Test de la métode crediter
     *
     * @throws IllegalFormatException
     */
    @Test
    public void testCrediterCompte() throws IllegalFormatException {
        /*
         * Méthode qui va tester la méthode crediter pour un compte quelconque
         * (pas important si avec ou sans découvert vu que les deux vont hériter
         * de la même méthode).
         */
        compte.crediter(100);
        assertEquals(100.0, compte.getSolde(), 0.001);
    }

    /**
     * Test de la métode crediter avec un montant négatif
     */
    @Test
    public void testCrediterCompteMontantNegatif() {
        /*
         * Méthode qui va tester la méthode crediter pour un compte quelconque
         * (pas important si avec ou sans découvert vu que les deux vont hériter
         * de la même méthode) avec un montant négatif, auquel cas il devrait
         * attraper un IllegalFormatExcepion
         */
        try {
            compte.crediter(-100);
            fail("La méthode n'a pas renvoyé d'exception!");
        } catch (IllegalFormatException ife) {
        } catch (Exception e) {
            fail("Exception de type " + e.getClass().getSimpleName()
                    + " récupérée alors qu'un IllegalFormatException était attendu");
        }
    }

    /**
     * Test du constructeur avec un format de compte volontairement faux pour
     * tester si une exception est renvoyée. Le détail des tests de conformité
     * du format de compte se font après
     */
    @Test
    public void testConstruireCompteAvecFormatNumeroCompteIncorrect() {
        try {
            compte = new CompteSansDecouvert("&éþ_ëüú¤", 0, client);
            fail("Exception non renvoyée par le constructeur avec un format de numéro de compte incorrect");
        } catch (IllegalFormatException ife) {
        } catch (Exception e) {
            fail("Exception de type " + e.getClass().getSimpleName()
                    + " récupérée à la place d'une de type IllegalFormatException");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteCorrect() {
        String strNumCompte = "FR0123456789";
        if (!Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " refusée dans le test");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteAvecUneSeuleLettreAuDebut() {
        String strNumCompte = "F0123456789";
        if (Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " validée dans le test");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteAucuneLettreAuDebut() {
        String strNumCompte = "0123456789";
        if (Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " validée dans le test");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteAvecTroisLettresAuDebut() {
        String strNumCompte = "FRA0123456789";
        if (Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " validée dans le test");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteAvecLettresAuMillieu() {
        String strNumCompte = "FR0123A456789";
        if (Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " validée dans le test");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteAvecPlusDeChiffresQueAttendu() {
        String strNumCompte = "FR00123456789";
        if (Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " validée dans le test");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteAvecMoinsDeChiffresQueAttendu() {
        String strNumCompte = "FR123456789";
        if (Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " validée dans le test");
        }
    }

    @Test
    public void testMethodeCheckFormatNumeroCompteAvecLettresALaFin() {
        String strNumCompte = "FR0123456789A";
        if (Compte.checkFormatNumeroCompte(strNumCompte)) {
            fail("String " + strNumCompte + " validée dans le test");
        }
    }

    @Test
    public void testNumeroCompteNull() throws IllegalFormatException {
        try {
            compte = new CompteSansDecouvert(null, 0, client);
            assertTrue("L'exception IllegalFormatException n'a pas été lancée !", false);
        } catch (IllegalFormatException e) {
            assertTrue(e instanceof IllegalFormatException);
        }
    }

    @Test
    public void testToString() throws IllegalFormatException {
        Map<String, Compte> comptes = new HashMap<>();
        comptes.put("AB7328887341", compte);

        client.setUserId("a.ade1");
        client.setNom("a");
        client.setPrenom("a");
        client.setAdresse("2 rue des cafards");
        client.setMale(true);
        client.setNumeroClient("9865432100");
        client.setUserPwd("aaa");
        client.setAccounts(comptes);

        String expected = "CompteSansDecouvert [numeroCompte=AB7328887341, solde=0.0, owner=Client [userId=" + client.getUserId()+ ", nom=" + client.getNom() + ", prenom=" + client.getPrenom() + ", adresse=" + client.getAdresse() + ", male=" + client.isMale() + ", userPwd=" + client.getUserPwd() + ", numeroClient=" + client.getNumeroClient() + ", accounts=" + client.getAccounts().size() + "]]";

        assertEquals(expected, compte.toString());
    }
}
