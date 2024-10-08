/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;

import jakarta.persistence.*;

// line 123 "../../../../../../GameCenter.ump"

@Entity
public class Cart
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------
    @Id
    @GeneratedValue
    private int id;

    //Cart Associations
    @ManyToOne
    private Game game;
    @ManyToOne
    private Client client;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Cart() {}

    public Cart(Game aGame, Client aClient)
    {
        if (!setGame(aGame))
        {
            throw new RuntimeException("Unable to create Cart due to aGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
        if (!setClient(aClient))
        {
            throw new RuntimeException("Unable to create Cart due to aClient. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
        }
    }

    //------------------------
    // INTERFACE
    //------------------------
    /* Code from template association_GetOne */
    public Game getGame()
    {
        return game;
    }
    /* Code from template association_GetOne */
    public Client getClient()
    {
        return client;
    }
    /* Code from template association_SetUnidirectionalOne */
    public boolean setGame(Game aNewGame)
    {
        boolean wasSet = false;
        if (aNewGame != null)
        {
            game = aNewGame;
            wasSet = true;
        }
        return wasSet;
    }
    /* Code from template association_SetUnidirectionalOne */
    public boolean setClient(Client aNewClient)
    {
        boolean wasSet = false;
        if (aNewClient != null)
        {
            client = aNewClient;
            wasSet = true;
        }
        return wasSet;
    }

    public void delete()
    {
        game = null;
        client = null;
    }

}
