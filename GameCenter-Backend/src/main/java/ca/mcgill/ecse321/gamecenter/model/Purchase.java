/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.Entity;

import java.util.*;
import java.sql.Date;

// line 99 "../../../../../../GameCenter.ump"
@Entity
public class Purchase
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Purchase> purchasesById = new HashMap<Integer, Purchase>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Purchase Attributes
  private int id;
  private float totalPrice;
  private int trackingCode;
  private Date purchaseDate;
  private String refundReason;

  //Purchase Associations
  private List<Game> associatedGames;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Purchase() {}

  public Purchase(int aId, float aTotalPrice, int aTrackingCode, Date aPurchaseDate, String aRefundReason, Game... allAssociatedGames)
  {
    totalPrice = aTotalPrice;
    trackingCode = aTrackingCode;
    purchaseDate = aPurchaseDate;
    refundReason = aRefundReason;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    associatedGames = new ArrayList<Game>();
    boolean didAddAssociatedGames = setAssociatedGames(allAssociatedGames);
    if (!didAddAssociatedGames)
    {
      throw new RuntimeException("Unable to create Purchase, must have at least 1 associatedGames. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    Integer anOldId = getId();
    if (anOldId != null && anOldId.equals(aId)) {
      return true;
    }
    if (hasWithId(aId)) {
      return wasSet;
    }
    id = aId;
    wasSet = true;
    if (anOldId != null) {
      purchasesById.remove(anOldId);
    }
    purchasesById.put(aId, this);
    return wasSet;
  }

  public boolean setTotalPrice(float aTotalPrice)
  {
    boolean wasSet = false;
    totalPrice = aTotalPrice;
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
  /* Code from template attribute_GetUnique */
  public static Purchase getWithId(int aId)
  {
    return purchasesById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public float getTotalPrice()
  {
    return totalPrice;
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
  /* Code from template association_GetMany */
  public Game getAssociatedGame(int index)
  {
    Game aAssociatedGame = associatedGames.get(index);
    return aAssociatedGame;
  }

  public List<Game> getAssociatedGames()
  {
    List<Game> newAssociatedGames = Collections.unmodifiableList(associatedGames);
    return newAssociatedGames;
  }

  public int numberOfAssociatedGames()
  {
    int number = associatedGames.size();
    return number;
  }

  public boolean hasAssociatedGames()
  {
    boolean has = associatedGames.size() > 0;
    return has;
  }

  public int indexOfAssociatedGame(Game aAssociatedGame)
  {
    int index = associatedGames.indexOf(aAssociatedGame);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAssociatedGames()
  {
    return 1;
  }
  /* Code from template association_AddUnidirectionalMStar */
  public boolean addAssociatedGame(Game aAssociatedGame)
  {
    boolean wasAdded = false;
    if (associatedGames.contains(aAssociatedGame)) { return false; }
    associatedGames.add(aAssociatedGame);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAssociatedGame(Game aAssociatedGame)
  {
    boolean wasRemoved = false;
    if (!associatedGames.contains(aAssociatedGame))
    {
      return wasRemoved;
    }

    if (numberOfAssociatedGames() <= minimumNumberOfAssociatedGames())
    {
      return wasRemoved;
    }

    associatedGames.remove(aAssociatedGame);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalMStar */
  public boolean setAssociatedGames(Game... newAssociatedGames)
  {
    boolean wasSet = false;
    ArrayList<Game> verifiedAssociatedGames = new ArrayList<Game>();
    for (Game aAssociatedGame : newAssociatedGames)
    {
      if (verifiedAssociatedGames.contains(aAssociatedGame))
      {
        continue;
      }
      verifiedAssociatedGames.add(aAssociatedGame);
    }

    if (verifiedAssociatedGames.size() != newAssociatedGames.length || verifiedAssociatedGames.size() < minimumNumberOfAssociatedGames())
    {
      return wasSet;
    }

    associatedGames.clear();
    associatedGames.addAll(verifiedAssociatedGames);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAssociatedGameAt(Game aAssociatedGame, int index)
  {  
    boolean wasAdded = false;
    if(addAssociatedGame(aAssociatedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssociatedGames()) { index = numberOfAssociatedGames() - 1; }
      associatedGames.remove(aAssociatedGame);
      associatedGames.add(index, aAssociatedGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAssociatedGameAt(Game aAssociatedGame, int index)
  {
    boolean wasAdded = false;
    if(associatedGames.contains(aAssociatedGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssociatedGames()) { index = numberOfAssociatedGames() - 1; }
      associatedGames.remove(aAssociatedGame);
      associatedGames.add(index, aAssociatedGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAssociatedGameAt(aAssociatedGame, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    purchasesById.remove(getId());
    associatedGames.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "totalPrice" + ":" + getTotalPrice()+ "," +
            "trackingCode" + ":" + getTrackingCode()+ "," +
            "refundReason" + ":" + getRefundReason()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "purchaseDate" + "=" + (getPurchaseDate() != null ? !getPurchaseDate().equals(this)  ? getPurchaseDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}