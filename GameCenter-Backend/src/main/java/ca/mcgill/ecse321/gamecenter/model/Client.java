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

  //Client Associations
  @OneToMany
  private List<Purchase> purchaseHistory;
  @OneToMany
  private List<PaymentInfo> paymentInformations;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Client() {}

  public Client(String aEmail, String aUsername, String aPassword, String aPhoneNumber, String aDeliveryAddress, int aNumberOfFlags)
  {
    super(aEmail, aUsername, aPassword);
    phoneNumber = aPhoneNumber;
    deliveryAddress = aDeliveryAddress;
    numberOfFlags = aNumberOfFlags;
    purchaseHistory = new ArrayList<Purchase>();
    paymentInformations = new ArrayList<PaymentInfo>();
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
  /* Code from template association_GetMany */
  public Purchase getPurchaseHistory(int index)
  {
    Purchase aPurchaseHistory = purchaseHistory.get(index);
    return aPurchaseHistory;
  }

  public List<Purchase> getPurchaseHistory()
  {
    List<Purchase> newPurchaseHistory = Collections.unmodifiableList(purchaseHistory);
    return newPurchaseHistory;
  }

  public int numberOfPurchaseHistory()
  {
    int number = purchaseHistory.size();
    return number;
  }

  public boolean hasPurchaseHistory()
  {
    boolean has = purchaseHistory.size() > 0;
    return has;
  }

  public int indexOfPurchaseHistory(Purchase aPurchaseHistory)
  {
    int index = purchaseHistory.indexOf(aPurchaseHistory);
    return index;
  }
  /* Code from template association_GetMany */
  public PaymentInfo getPaymentInformation(int index)
  {
    PaymentInfo aPaymentInformation = paymentInformations.get(index);
    return aPaymentInformation;
  }

  public List<PaymentInfo> getPaymentInformations()
  {
    List<PaymentInfo> newPaymentInformations = Collections.unmodifiableList(paymentInformations);
    return newPaymentInformations;
  }

  public int numberOfPaymentInformations()
  {
    int number = paymentInformations.size();
    return number;
  }

  public boolean hasPaymentInformations()
  {
    boolean has = paymentInformations.size() > 0;
    return has;
  }

  public int indexOfPaymentInformation(PaymentInfo aPaymentInformation)
  {
    int index = paymentInformations.indexOf(aPaymentInformation);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPurchaseHistory()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addPurchaseHistory(Purchase aPurchaseHistory)
  {
    boolean wasAdded = false;
    if (purchaseHistory.contains(aPurchaseHistory)) { return false; }
    purchaseHistory.add(aPurchaseHistory);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePurchaseHistory(Purchase aPurchaseHistory)
  {
    boolean wasRemoved = false;
    if (purchaseHistory.contains(aPurchaseHistory))
    {
      purchaseHistory.remove(aPurchaseHistory);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPurchaseHistoryAt(Purchase aPurchaseHistory, int index)
  {
    boolean wasAdded = false;
    if(addPurchaseHistory(aPurchaseHistory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPurchaseHistory()) { index = numberOfPurchaseHistory() - 1; }
      purchaseHistory.remove(aPurchaseHistory);
      purchaseHistory.add(index, aPurchaseHistory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePurchaseHistoryAt(Purchase aPurchaseHistory, int index)
  {
    boolean wasAdded = false;
    if(purchaseHistory.contains(aPurchaseHistory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPurchaseHistory()) { index = numberOfPurchaseHistory() - 1; }
      purchaseHistory.remove(aPurchaseHistory);
      purchaseHistory.add(index, aPurchaseHistory);
      wasAdded = true;
    }
    else
    {
      wasAdded = addPurchaseHistoryAt(aPurchaseHistory, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPaymentInformations()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addPaymentInformation(PaymentInfo aPaymentInformation)
  {
    boolean wasAdded = false;
    if (paymentInformations.contains(aPaymentInformation)) { return false; }
    paymentInformations.add(aPaymentInformation);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePaymentInformation(PaymentInfo aPaymentInformation)
  {
    boolean wasRemoved = false;
    if (paymentInformations.contains(aPaymentInformation))
    {
      paymentInformations.remove(aPaymentInformation);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPaymentInformationAt(PaymentInfo aPaymentInformation, int index)
  {
    boolean wasAdded = false;
    if(addPaymentInformation(aPaymentInformation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaymentInformations()) { index = numberOfPaymentInformations() - 1; }
      paymentInformations.remove(aPaymentInformation);
      paymentInformations.add(index, aPaymentInformation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePaymentInformationAt(PaymentInfo aPaymentInformation, int index)
  {
    boolean wasAdded = false;
    if(paymentInformations.contains(aPaymentInformation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaymentInformations()) { index = numberOfPaymentInformations() - 1; }
      paymentInformations.remove(aPaymentInformation);
      paymentInformations.add(index, aPaymentInformation);
      wasAdded = true;
    }
    else
    {
      wasAdded = addPaymentInformationAt(aPaymentInformation, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    purchaseHistory.clear();
    paymentInformations.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "deliveryAddress" + ":" + getDeliveryAddress()+ "," +
            "numberOfFlags" + ":" + getNumberOfFlags()+ "]";
  }
}