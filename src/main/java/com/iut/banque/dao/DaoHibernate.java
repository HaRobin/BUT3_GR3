package com.iut.banque.dao;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.TechnicalException;
import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Implémentation de IDao utilisant Hibernate.
 * <p>
 * Les transactions sont gerés par Spring et utilise le transaction manager
 * défini dans l'application Context.
 * <p>
 * Par défaut, la propagation des transactions est REQUIRED, ce qui signifie que
 * si une transaction est déjà commencé elle va être réutilisée. Cela est util
 * pour les tests unitaires de la DAO.
 */
@Transactional
public class DaoHibernate implements IDao {
    transient Logger logger = Logger.getLogger(getClass().getName());

    private SessionFactory sessionFactory;

    public DaoHibernate() {
        logger.info("==================");
        logger.info("Création de la Dao");
    }

    /**
     * Setter pour la SessionFactory.
     * <p>
     * Cette méthode permet à Spring d'injecter la factory au moment de la
     * construction de la DAO.
     *
     * @param sessionFactory : la session factory nécessaire à la gestion des sessions
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalOperationException
     */
    @Override
    public CompteAvecDecouvert createCompteAvecDecouvert(double solde, String numeroCompte, double decouvertAutorise,
                                                         Client client) throws TechnicalException, IllegalFormatException, IllegalOperationException {
        Session session = sessionFactory.getCurrentSession();
        CompteAvecDecouvert compte = session.get(CompteAvecDecouvert.class, numeroCompte);
        if (compte != null) {
            throw new TechnicalException("Numéro de compte déjà utilisé.");
        }

        compte = new CompteAvecDecouvert(numeroCompte, solde, decouvertAutorise, client);
        client.addAccount(compte);
        session.save(compte);

        return compte;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompteSansDecouvert createCompteSansDecouvert(double solde, String numeroCompte, Client client)
            throws TechnicalException, IllegalFormatException {
        Session session = sessionFactory.getCurrentSession();
        CompteSansDecouvert compte = session.get(CompteSansDecouvert.class, numeroCompte);
        if (compte != null) {
            throw new TechnicalException("Numéro de compte déjà utilisé.");
        }

        compte = new CompteSansDecouvert(numeroCompte, solde, client);
        session.save(compte);
        client.addAccount(compte);
        session.save(compte);

        return compte;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAccount(Compte c) {
        Session session = sessionFactory.getCurrentSession();
        session.update(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAccount(Compte c) throws TechnicalException {
        Session session = sessionFactory.getCurrentSession();
        if (c == null) {
            throw new TechnicalException("Ce compte n'existe plus");
        }
        session.delete(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Compte> getAccountsByClientId(String id) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, id);
        if (client != null) {
            return client.getAccounts();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Compte getAccountById(String id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Compte.class, id);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalFormatException
     * @throws IllegalArgumentException
     */
    @Override
    public Utilisateur createUser(String nom, String prenom, String adresse, boolean male, String userId,
                                  String userPwd, boolean manager, String numClient)
            throws TechnicalException, IllegalArgumentException, IllegalFormatException {
        Session session = sessionFactory.getCurrentSession();

        Utilisateur user = session.get(Utilisateur.class, userId);
        if (user != null) {
            throw new TechnicalException("User Id déjà utilisé.");
        }

        if (manager) {
            user = new Gestionnaire(nom, prenom, adresse, male, userId, userPwd);
        } else {
            user = new Client(nom, prenom, adresse, male, userId, userPwd, numClient);
        }
        session.save(user);

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(Utilisateur u) throws TechnicalException {
        Session session = sessionFactory.getCurrentSession();
        if (u == null) {
            throw new TechnicalException("Cet utilisateur n'existe plus");
        }
        session.delete(u);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(Utilisateur u) {
        Session session = sessionFactory.getCurrentSession();
        session.update(u);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserAllowed(String userId, String userPwd) {
        Session session = null;
        if (userId == null || userPwd == null) {
            return false;
        } else {
            sessionFactory.openSession();
            userId = userId.trim();
            if ("".equals(userId) || "".equals(userPwd)) {
                return false;
            } else {
                session = sessionFactory.getCurrentSession();
                Utilisateur user = session.get(Utilisateur.class, userId);
                if (user == null) {
                    return false;
                }
                return (userPwd.equals(user.getUserPwd()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utilisateur getUserById(String id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Utilisateur.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Client> getAllClients() {
        Session session = sessionFactory.getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Object> res = session.createCriteria(Client.class).list();
        Map<String, Client> ret = new HashMap<>();
        for (Object client : res) {
            ret.put(((Client) client).getUserId(), (Client) client);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Gestionnaire> getAllGestionnaires() {
        Session session = sessionFactory.getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Object> res = session.createCriteria(Gestionnaire.class).list();
        Map<String, Gestionnaire> ret = new HashMap<>();
        for (Object gestionnaire : res) {
            ret.put(((Gestionnaire) gestionnaire).getUserId(), (Gestionnaire) gestionnaire);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        logger.info("Déconnexion de la DAO.");
    }

}
