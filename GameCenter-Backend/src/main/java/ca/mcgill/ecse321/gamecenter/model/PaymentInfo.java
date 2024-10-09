/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// line 116 "../../../../../../GameCenter.ump"
@Entity
public class PaymentInfo
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentInfo Attributes
  @Id
  @GeneratedValue
  private int id;
  private String cardNumber;
  private int cvv;
  private int expiryMonth;
  private int expiryYear;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentInfo() {}

  public PaymentInfo(String aCardNumber, int aCvv, int aExpiryMonth, int aExpiryYear)
  {
    cardNumber = aCardNumber;
    cvv = aCvv;
    expiryMonth = aExpiryMonth;
    expiryYear = aExpiryYear;
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

  public boolean setCardNumber(String aCardNumber)
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

  public String getCardNumber()
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
  {}


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