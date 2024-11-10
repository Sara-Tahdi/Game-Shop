/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

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
  private String cvv;
  private int expiryMonth;
  private int expiryYear;

  //PaymentInfo Associations
  @ManyToOne
  private Client client;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentInfo() {}

  public PaymentInfo(String aCardNumber, String aCvv, int aExpiryMonth, int aExpiryYear, Client aClient)
  {
    cardNumber = aCardNumber;
    cvv = aCvv;
    expiryMonth = aExpiryMonth;
    expiryYear = aExpiryYear;
    if (!setClient(aClient))
    {
      throw new RuntimeException("Unable to create PaymentInfo due to aClient. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setCardNumber(String aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setCvv(String aCvv)
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

  public String getCvv()
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

  /* Code from template association_GetOne */
  public Client getClient()
  {
    return client;
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
    client = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "cardNumber" + ":" + getCardNumber()+ "," +
            "cvv" + ":" + getCvv()+ "," +
            "expiryMonth" + ":" + getExpiryMonth()+ "," +
            "expiryYear" + ":" + getExpiryYear()+ "]" + System.getProperties().getProperty("line.separator") +
    "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null");
  }
}
