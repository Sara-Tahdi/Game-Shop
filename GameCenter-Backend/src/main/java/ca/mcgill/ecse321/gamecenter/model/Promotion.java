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

  //----------------
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

  public Promotion(float aNewPrice, Date aStartDate, Date aEndDate)
  {
    newPrice = aNewPrice;
    startDate = aStartDate;
    endDate = aEndDate;
  }

  public Promotion() {}

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
    id = aId;
    wasSet = true;
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



  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "newPrice" + ":" + getNewPrice()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this)  ? getStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endDate" + "=" + (getEndDate() != null ? !getEndDate().equals(this)  ? getEndDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}