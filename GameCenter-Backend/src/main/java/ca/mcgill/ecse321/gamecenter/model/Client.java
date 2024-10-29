package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.*;

import java.util.*;

// line 32 "../../../../../../GameCenter.ump"
@Entity
@DiscriminatorValue("CLIENT")
public class Client extends AppUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Client Attributes
  private String phoneNumber;
  private String deliveryAddress;
  private int numberOfFlags;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Client() {
  }

  public Client(String aEmail, String aUsername, String aPassword, String aPhoneNumber, String aDeliveryAddress, int aNumberOfFlags)
  {
    super(aEmail, aUsername, aPassword);
    phoneNumber = aPhoneNumber;
    deliveryAddress = aDeliveryAddress;
    numberOfFlags = aNumberOfFlags;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setDeliveryAddress(String aDeliveryAddress)
  {
    boolean wasSet = false;
    deliveryAddress = aDeliveryAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumberOfFlags(int aNumberOfFlags)
  {
    boolean wasSet = false;
    numberOfFlags = aNumberOfFlags;
    wasSet = true;
    return wasSet;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getDeliveryAddress()
  {
    return deliveryAddress;
  }

  public int getNumberOfFlags()
  {
    return numberOfFlags;
  }



  public void delete()
  {
  }


  public String toString()
  {
    return super.toString() + "["+
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "deliveryAddress" + ":" + getDeliveryAddress()+ "," +
            "numberOfFlags" + ":" + getNumberOfFlags()+ "]";
  }
}