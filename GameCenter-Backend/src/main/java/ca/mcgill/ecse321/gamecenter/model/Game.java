/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.*;

import java.util.*;

// line 50 "../../../../../../GameCenter.ump"
@Entity
public class Game
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum GeneralFeeling { VERYPOSITIVE, POSITIVE, NEUTRAL, NEGATIVE, VERYNEGATIVE }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  @Id
  @GeneratedValue
  private int id;
  private String title;
  private float price;
  private String description;
  private float rating;
  private int remainingQuantity;
  private boolean isOffered;
  private GeneralFeeling publicOpinion;

  //Game Associations
  @OneToMany(
          cascade = CascadeType.ALL,
          fetch = FetchType.EAGER
  )
  private List<Review> reviews;
  @OneToMany(
          cascade = CascadeType.ALL,
          fetch = FetchType.EAGER
  )
  private List<Promotion> promotions;
  @ManyToOne
  private GameCategory categories;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game() {}

  public Game(String aTitle, float aPrice, String aDescription, float aRating, int aRemainingQuantity, boolean aIsOffered, GeneralFeeling aPublicOpinion, GameCategory aCategories)
  {
    title = aTitle;
    price = aPrice;
    description = aDescription;
    rating = aRating;
    remainingQuantity = aRemainingQuantity;
    isOffered = aIsOffered;
    publicOpinion = aPublicOpinion;
    reviews = new ArrayList<Review>();
    promotions = new ArrayList<Promotion>();
    if (!setCategories(aCategories))
    {
      throw new RuntimeException("Unable to create Game due to aCategories. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(float aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setRating(float aRating)
  {
    boolean wasSet = false;
    rating = aRating;
    wasSet = true;
    return wasSet;
  }

  public boolean setRemainingQuantity(int aRemainingQuantity)
  {
    boolean wasSet = false;
    remainingQuantity = aRemainingQuantity;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsOffered(boolean aIsOffered)
  {
    boolean wasSet = false;
    isOffered = aIsOffered;
    wasSet = true;
    return wasSet;
  }

  public boolean setPublicOpinion(GeneralFeeling aPublicOpinion)
  {
    boolean wasSet = false;
    publicOpinion = aPublicOpinion;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public String getTitle()
  {
    return title;
  }

  public float getPrice()
  {
    return price;
  }

  public String getDescription()
  {
    return description;
  }

  public float getRating()
  {
    return rating;
  }

  public int getRemainingQuantity()
  {
    return remainingQuantity;
  }

  public boolean getIsOffered()
  {
    return isOffered;
  }

  public GeneralFeeling getPublicOpinion()
  {
    return publicOpinion;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsOffered()
  {
    return isOffered;
  }
  /* Code from template association_GetMany */
  public Review getReview(int index)
  {
    Review aReview = reviews.get(index);
    return aReview;
  }

  public List<Review> getReviews()
  {
    List<Review> newReviews = Collections.unmodifiableList(reviews);
    return newReviews;
  }

  public int numberOfReviews()
  {
    int number = reviews.size();
    return number;
  }

  public boolean hasReviews()
  {
    boolean has = reviews.size() > 0;
    return has;
  }

  public int indexOfReview(Review aReview)
  {
    int index = reviews.indexOf(aReview);
    return index;
  }
  /* Code from template association_GetMany */
  public Promotion getPromotion(int index)
  {
    Promotion aPromotion = promotions.get(index);
    return aPromotion;
  }

  public List<Promotion> getPromotions()
  {
    List<Promotion> newPromotions = Collections.unmodifiableList(promotions);
    return newPromotions;
  }

  public int numberOfPromotions()
  {
    int number = promotions.size();
    return number;
  }

  public boolean hasPromotions()
  {
    boolean has = promotions.size() > 0;
    return has;
  }

  public int indexOfPromotion(Promotion aPromotion)
  {
    int index = promotions.indexOf(aPromotion);
    return index;
  }
  /* Code from template association_GetOne */
  public GameCategory getCategories()
  {
    return categories;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReviews()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addReview(Review aReview)
  {
    boolean wasAdded = false;
    if (reviews.contains(aReview)) { return false; }
    reviews.add(aReview);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReview(Review aReview)
  {
    boolean wasRemoved = false;
    if (reviews.contains(aReview))
    {
      reviews.remove(aReview);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReviewAt(Review aReview, int index)
  {
    boolean wasAdded = false;
    if(addReview(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReviewAt(Review aReview, int index)
  {
    boolean wasAdded = false;
    if(reviews.contains(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReviews()) { index = numberOfReviews() - 1; }
      reviews.remove(aReview);
      reviews.add(index, aReview);
      wasAdded = true;
    }
    else
    {
      wasAdded = addReviewAt(aReview, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotions()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addPromotion(Promotion aPromotion)
  {
    boolean wasAdded = false;
    if (promotions.contains(aPromotion)) { return false; }
    promotions.add(aPromotion);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePromotion(Promotion aPromotion)
  {
    boolean wasRemoved = false;
    if (promotions.contains(aPromotion))
    {
      promotions.remove(aPromotion);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPromotionAt(Promotion aPromotion, int index)
  {
    boolean wasAdded = false;
    if(addPromotion(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotions()) { index = numberOfPromotions() - 1; }
      promotions.remove(aPromotion);
      promotions.add(index, aPromotion);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePromotionAt(Promotion aPromotion, int index)
  {
    boolean wasAdded = false;
    if(promotions.contains(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotions()) { index = numberOfPromotions() - 1; }
      promotions.remove(aPromotion);
      promotions.add(index, aPromotion);
      wasAdded = true;
    }
    else
    {
      wasAdded = addPromotionAt(aPromotion, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCategories(GameCategory aNewCategories)
  {
    boolean wasSet = false;
    if (aNewCategories != null)
    {
      categories = aNewCategories;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    reviews.clear();
    promotions.clear();
    categories = null;
  }


  @SuppressWarnings("unlikely-arg-type")
  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "title" + ":" + getTitle()+ "," +
            "price" + ":" + getPrice()+ "," +
            "description" + ":" + getDescription()+ "," +
            "rating" + ":" + getRating()+ "," +
            "remainingQuantity" + ":" + getRemainingQuantity()+ "," +
            "isOffered" + ":" + getIsOffered()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "publicOpinion" + "=" + (getPublicOpinion() != null ? !getPublicOpinion().equals(this)  ? getPublicOpinion().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "categories = "+(getCategories()!=null?Integer.toHexString(System.identityHashCode(getCategories())):"null");
  }
}