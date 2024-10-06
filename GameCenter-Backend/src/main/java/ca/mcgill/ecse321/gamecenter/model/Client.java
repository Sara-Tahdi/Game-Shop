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
  @OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Purchase> purchaseHistory;

  @ManyToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Game> wishlist;

  @ManyToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Game> cart;

  @OneToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<PaymentInfo> paymentInformations;

  @ManyToMany(
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY
  )
  private List<Review> thumbsUp;

  @ManyToMany(
          cascade = CascadeType.ALL,
          fetch = FetchType.LAZY
  )
  private List<Review> thumbsDown;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Client() {
    super();
  }

  public Client(int aId, String aEmail, String aUsername, String aPassword, boolean aIsActive, String aPhoneNumber, String aDeliveryAddress, int aNumberOfFlags)
  {
    super(aId, aEmail, aUsername, aPassword, aIsActive);
    phoneNumber = aPhoneNumber;
    deliveryAddress = aDeliveryAddress;
    numberOfFlags = aNumberOfFlags;
    purchaseHistory = new ArrayList<Purchase>();
    wishlist = new ArrayList<Game>();
    cart = new ArrayList<Game>();
    paymentInformations = new ArrayList<PaymentInfo>();
    thumbsUp = new ArrayList<Review>();
    thumbsDown = new ArrayList<Review>();
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
  public Game getWishlist(int index)
  {
    Game aWishlist = wishlist.get(index);
    return aWishlist;
  }

  public List<Game> getWishlist()
  {
    List<Game> newWishlist = Collections.unmodifiableList(wishlist);
    return newWishlist;
  }

  public int numberOfWishlist()
  {
    int number = wishlist.size();
    return number;
  }

  public boolean hasWishlist()
  {
    boolean has = wishlist.size() > 0;
    return has;
  }

  public int indexOfWishlist(Game aWishlist)
  {
    int index = wishlist.indexOf(aWishlist);
    return index;
  }
  /* Code from template association_GetMany */
  public Game getCart(int index)
  {
    Game aCart = cart.get(index);
    return aCart;
  }

  public List<Game> getCart()
  {
    List<Game> newCart = Collections.unmodifiableList(cart);
    return newCart;
  }

  public int numberOfCart()
  {
    int number = cart.size();
    return number;
  }

  public boolean hasCart()
  {
    boolean has = cart.size() > 0;
    return has;
  }

  public int indexOfCart(Game aCart)
  {
    int index = cart.indexOf(aCart);
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
  /* Code from template association_GetMany */
  public Review getThumbsUp(int index)
  {
    Review aThumbsUp = thumbsUp.get(index);
    return aThumbsUp;
  }

  public List<Review> getThumbsUp()
  {
    List<Review> newThumbsUp = Collections.unmodifiableList(thumbsUp);
    return newThumbsUp;
  }

  public int numberOfThumbsUp()
  {
    int number = thumbsUp.size();
    return number;
  }

  public boolean hasThumbsUp()
  {
    boolean has = thumbsUp.size() > 0;
    return has;
  }

  public int indexOfThumbsUp(Review aThumbsUp)
  {
    int index = thumbsUp.indexOf(aThumbsUp);
    return index;
  }
  /* Code from template association_GetMany */
  public Review getThumbsDown(int index)
  {
    Review aThumbsDown = thumbsDown.get(index);
    return aThumbsDown;
  }

  public List<Review> getThumbsDown()
  {
    List<Review> newThumbsDown = Collections.unmodifiableList(thumbsDown);
    return newThumbsDown;
  }

  public int numberOfThumbsDown()
  {
    int number = thumbsDown.size();
    return number;
  }

  public boolean hasThumbsDown()
  {
    boolean has = thumbsDown.size() > 0;
    return has;
  }

  public int indexOfThumbsDown(Review aThumbsDown)
  {
    int index = thumbsDown.indexOf(aThumbsDown);
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
  public static int minimumNumberOfWishlist()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addWishlist(Game aWishlist)
  {
    boolean wasAdded = false;
    if (wishlist.contains(aWishlist)) { return false; }
    wishlist.add(aWishlist);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWishlist(Game aWishlist)
  {
    boolean wasRemoved = false;
    if (wishlist.contains(aWishlist))
    {
      wishlist.remove(aWishlist);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWishlistAt(Game aWishlist, int index)
  {  
    boolean wasAdded = false;
    if(addWishlist(aWishlist))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlist()) { index = numberOfWishlist() - 1; }
      wishlist.remove(aWishlist);
      wishlist.add(index, aWishlist);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishlistAt(Game aWishlist, int index)
  {
    boolean wasAdded = false;
    if(wishlist.contains(aWishlist))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishlist()) { index = numberOfWishlist() - 1; }
      wishlist.remove(aWishlist);
      wishlist.add(index, aWishlist);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWishlistAt(aWishlist, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCart()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCart(Game aCart)
  {
    boolean wasAdded = false;
    if (cart.contains(aCart)) { return false; }
    cart.add(aCart);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCart(Game aCart)
  {
    boolean wasRemoved = false;
    if (cart.contains(aCart))
    {
      cart.remove(aCart);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCartAt(Game aCart, int index)
  {  
    boolean wasAdded = false;
    if(addCart(aCart))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCart()) { index = numberOfCart() - 1; }
      cart.remove(aCart);
      cart.add(index, aCart);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCartAt(Game aCart, int index)
  {
    boolean wasAdded = false;
    if(cart.contains(aCart))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCart()) { index = numberOfCart() - 1; }
      cart.remove(aCart);
      cart.add(index, aCart);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCartAt(aCart, index);
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfThumbsUp()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addThumbsUp(Review aThumbsUp)
  {
    boolean wasAdded = false;
    if (thumbsUp.contains(aThumbsUp)) { return false; }
    thumbsUp.add(aThumbsUp);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeThumbsUp(Review aThumbsUp)
  {
    boolean wasRemoved = false;
    if (thumbsUp.contains(aThumbsUp))
    {
      thumbsUp.remove(aThumbsUp);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addThumbsUpAt(Review aThumbsUp, int index)
  {  
    boolean wasAdded = false;
    if(addThumbsUp(aThumbsUp))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfThumbsUp()) { index = numberOfThumbsUp() - 1; }
      thumbsUp.remove(aThumbsUp);
      thumbsUp.add(index, aThumbsUp);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveThumbsUpAt(Review aThumbsUp, int index)
  {
    boolean wasAdded = false;
    if(thumbsUp.contains(aThumbsUp))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfThumbsUp()) { index = numberOfThumbsUp() - 1; }
      thumbsUp.remove(aThumbsUp);
      thumbsUp.add(index, aThumbsUp);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addThumbsUpAt(aThumbsUp, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfThumbsDown()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addThumbsDown(Review aThumbsDown)
  {
    boolean wasAdded = false;
    if (thumbsDown.contains(aThumbsDown)) { return false; }
    thumbsDown.add(aThumbsDown);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeThumbsDown(Review aThumbsDown)
  {
    boolean wasRemoved = false;
    if (thumbsDown.contains(aThumbsDown))
    {
      thumbsDown.remove(aThumbsDown);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addThumbsDownAt(Review aThumbsDown, int index)
  {  
    boolean wasAdded = false;
    if(addThumbsDown(aThumbsDown))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfThumbsDown()) { index = numberOfThumbsDown() - 1; }
      thumbsDown.remove(aThumbsDown);
      thumbsDown.add(index, aThumbsDown);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveThumbsDownAt(Review aThumbsDown, int index)
  {
    boolean wasAdded = false;
    if(thumbsDown.contains(aThumbsDown))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfThumbsDown()) { index = numberOfThumbsDown() - 1; }
      thumbsDown.remove(aThumbsDown);
      thumbsDown.add(index, aThumbsDown);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addThumbsDownAt(aThumbsDown, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    purchaseHistory.clear();
    wishlist.clear();
    cart.clear();
    paymentInformations.clear();
    thumbsUp.clear();
    thumbsDown.clear();
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "deliveryAddress" + ":" + getDeliveryAddress()+ "," +
            "numberOfFlags" + ":" + getNumberOfFlags()+ "]";
  }
}