package com.iut.banque.converter;

import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.Client;
import com.opensymphony.xwork2.conversion.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Cette classe contient des méthodes permettant de convertir un client en
 * string et vice-versa. Elle est déclarée dans
 * «src/main/webapp/WEB-INF/classes/xwork-conversion.properties.
 * <p>
 * Grâce à cette classe il est possible de passer en paramêtre d'une action
 * Struts l'identifiant d'un client (une string) et le contrôleur qui va
 * recevoir le paramêtre n'a besoin que d'un setter prenant un objet de type
 * Client.
 */
public class ClientConverter extends StrutsTypeConverter {

    /**
     * DAO utilisée pour récuperer les objets correspondants à l'id passé en
     * paramêtre de convertFromString.
     * <p>
     * Note : Ce champ est static car pour une raison qui nous échappe, le scope
     * « singleton » du bean Spring utilisé pour l'injection n'est pas respecté.
     * Ainsi, au chargement de l'application, trois objets de cette classe sont
     * instanciés et seulement le premier a une DAO injectée correctement.
     */

    private Logger logger = Logger.getLogger(ClientConverter.class.getName());
    private IDao dao;

    /**
     * Constructeur avec paramêtre pour le ClientConverter.
     * <p>
     * Utilisé pour l'injection de dépendance.
     *
     * @param dao
     */
    public ClientConverter(IDao dao) {
        logger.info("=========================");
        logger.info("Création du convertisseur de client");
        this.dao = dao;
    }

    /**
     * Constructeur sans paramêtre pour le ClientConverter
     */
    public ClientConverter() {
        logger.info("=========================");
        logger.info("Création du convertisseur de client");
    }

    /**
     * Permet la conversion automatique par Struts d'un tableau de chaine vers
     * un Client
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object convertFromString(Map context, String[] values, Class classe) {
        Client client = null;
        client = (Client) dao.getUserById(values[0]);
        if (client == null) {
            throw new TypeConversionException("Impossible de convertir la chaine suivante : " + values[0]);
        }
        return client;
    }

    /**
     * Permet la conversion automatique par Struts d'un Client vers une chaine
     */
    @SuppressWarnings("rawtypes")
    @Override
    public String convertToString(Map context, Object value) {
        Client client = (Client) value;
        return client == null ? null : client.getIdentity();
    }

}
