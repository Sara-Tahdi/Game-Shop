/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.*;
import java.sql.Date;

// line 47 "../../../../../../GameCenter.ump"
@Entity
public class Promotion
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Promotion> promotionsById = new HashMap<Integer, Promotion>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Promotion Attributes
  @Id
  @GeneratedValue
  private int id;
  private float newPrice;
  private Date startDate;
  private Date endDate;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Promotion(int aId, float aNewPrice, Date aStartDate, Date aEndDate)
  {
    newPrice = aNewPrice;
    startDate = aStartDate;
    endDate = aEndDate;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
  }

  public Promotion() {
    // TODO
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
      promotionsById.remove(anOldId);
    }
    promotionsById.put(aId, this);
    return wasSet;
  }

  public boolean setNewPrice(float aNewPrice)
  {
    boolean wasSet = false;
    newPrice = aNewPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDate(Date aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDate(Date aEndDate)
  {
    boolean wasSet = false;
    endDate = aEndDate;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Promotion getWithId(int aId)
  {
    return promotionsById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public float getNewPrice()
  {
    return newPrice;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void delete()
  {
    promotionsById.remove(getId());
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "newPrice" + ":" + getNewPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this)  ? getStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endDate" + "=" + (getEndDate() != null ? !getEndDate().equals(this)  ? getEndDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}