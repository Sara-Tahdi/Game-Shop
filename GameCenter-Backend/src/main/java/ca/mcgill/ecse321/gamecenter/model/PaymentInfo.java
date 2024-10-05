/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.*;

// line 120 "../../../../../../GameCenter.ump"

@Entity
public class PaymentInfo
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, PaymentInfo> paymentinfosById = new HashMap<Integer, PaymentInfo>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentInfo Attributes
  @Id
  @GeneratedValue
  private int id;
  private int cardNumber;
  private int cvv;
  private int expiryMonth;
  private int expiryYear;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentInfo() {}

  public PaymentInfo(int aId, int aCardNumber, int aCvv, int aExpiryMonth, int aExpiryYear)
  {
    cardNumber = aCardNumber;
    cvv = aCvv;
    expiryMonth = aExpiryMonth;
    expiryYear = aExpiryYear;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
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
      paymentinfosById.remove(anOldId);
    }
    paymentinfosById.put(aId, this);
    return wasSet;
  }

  public boolean setCardNumber(int aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setCvv(int aCvv)
  {
    boolean wasSet = false;
    cvv = aCvv;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpiryMonth(int aExpiryMonth)
  {
    boolean wasSet = false;
    expiryMonth = aExpiryMonth;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpiryYear(int aExpiryYear)
  {
    boolean wasSet = false;
    expiryYear = aExpiryYear;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static PaymentInfo getWithId(int aId)
  {
    return paymentinfosById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public int getCardNumber()
  {
    return cardNumber;
  }

  public int getCvv()
  {
    return cvv;
  }

  public int getExpiryMonth()
  {
    return expiryMonth;
  }

  public int getExpiryYear()
  {
    return expiryYear;
  }

  public void delete()
  {
    paymentinfosById.remove(getId());
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "cardNumber" + ":" + getCardNumber()+ "," +
            "cvv" + ":" + getCvv()+ "," +
            "expiryMonth" + ":" + getExpiryMonth()+ "," +
            "expiryYear" + ":" + getExpiryYear()+ "]";
  }
}