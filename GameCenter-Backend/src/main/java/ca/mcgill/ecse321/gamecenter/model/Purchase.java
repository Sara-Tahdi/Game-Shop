/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.*;

import java.sql.Date;

// line 99 "../../../../../../GameCenter.ump"
@Entity
public class Purchase
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Purchase Attributes
  @Id
  @GeneratedValue
  private int id;
  private float totalPrice;
  private int copies;
  private int trackingCode;
  private Date purchaseDate;
  private String refundReason;

  //Purchase Associations
  @ManyToOne
  private Game game;
  @ManyToOne
  private Client client;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Purchase() {}

  public Purchase(float aTotalPrice, int aCopies, int aTrackingCode, Date aPurchaseDate, Game aGame, Client aClient)
  {
    totalPrice = aTotalPrice;
    copies = aCopies;
    trackingCode = aTrackingCode;
    purchaseDate = aPurchaseDate;
    if (!setGame(aGame))
    {
      throw new RuntimeException("Unable to create Purchase due to aGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setClient(aClient))
    {
      throw new RuntimeException("Unable to create Purchase due to aClient. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setTotalPrice(float aTotalPrice)
  {
    boolean wasSet = false;
    totalPrice = aTotalPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setCopies(int aCopies)
  {
    boolean wasSet = false;
    copies = aCopies;
    wasSet = true;
    return wasSet;
  }

  public boolean setTrackingCode(int aTrackingCode)
  {
    boolean wasSet = false;
    trackingCode = aTrackingCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setPurchaseDate(Date aPurchaseDate)
  {
    boolean wasSet = false;
    purchaseDate = aPurchaseDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setRefundReason(String aRefundReason)
  {
    boolean wasSet = false;
    refundReason = aRefundReason;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public float getTotalPrice()
  {
    return totalPrice;
  }

  public int getCopies()
  {
    return copies;
  }

  public int getTrackingCode()
  {
    return trackingCode;
  }

  public Date getPurchaseDate()
  {
    return purchaseDate;
  }

  public String getRefundReason()
  {
    return refundReason;
  }
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


  @SuppressWarnings("unlikely-arg-type")
  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "totalPrice" + ":" + getTotalPrice()+ "," +
            "copies" + ":" + getCopies()+ "," +
            "trackingCode" + ":" + getTrackingCode()+ "," +
            "refundReason" + ":" + getRefundReason()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null");
  }
}