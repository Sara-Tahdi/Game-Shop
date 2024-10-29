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

  @ManyToOne
  private GameCategory category;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game() {}

  public Game(String aTitle, float aPrice, String aDescription, float aRating, int aRemainingQuantity, boolean aIsOffered, GeneralFeeling aPublicOpinion, GameCategory aCategory)
  {
    title = aTitle;
    price = aPrice;
    description = aDescription;
    rating = aRating;
    remainingQuantity = aRemainingQuantity;
    isOffered = aIsOffered;
    publicOpinion = aPublicOpinion;
    if (!setCategory(aCategory))
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




  /* Code from template association_GetOne */
  public GameCategory getCategory()
  {
    return category;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setCategory(GameCategory aNewCategory)
  {
    boolean wasSet = false;
    if (aNewCategory != null)
    {
      category = aNewCategory;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    category = null;
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
            "  " + "categories = "+(getCategory()!=null?Integer.toHexString(System.identityHashCode(getCategory())):"null");
  }
}