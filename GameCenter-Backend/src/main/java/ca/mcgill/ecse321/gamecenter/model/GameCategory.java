/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.*;

// line 71 "../../../../../../GameCenter.ump"
@Entity
public class GameCategory
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, GameCategory> gamecategorysById = new HashMap<Integer, GameCategory>();
  private static Map<String, GameCategory> gamecategorysByCategory = new HashMap<String, GameCategory>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameCategory Attributes
  @Id
  @GeneratedValue
  private int id;
  private String category;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameCategory(int aId, String aCategory)
  {
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setCategory(aCategory))
    {
      throw new RuntimeException("Cannot create due to duplicate category. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
  }

  public GameCategory() {
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
      gamecategorysById.remove(anOldId);
    }
    gamecategorysById.put(aId, this);
    return wasSet;
  }

  public boolean setCategory(String aCategory)
  {
    boolean wasSet = false;
    String anOldCategory = getCategory();
    if (anOldCategory != null && anOldCategory.equals(aCategory)) {
      return true;
    }
    if (hasWithCategory(aCategory)) {
      return wasSet;
    }
    category = aCategory;
    wasSet = true;
    if (anOldCategory != null) {
      gamecategorysByCategory.remove(anOldCategory);
    }
    gamecategorysByCategory.put(aCategory, this);
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static GameCategory getWithId(int aId)
  {
    return gamecategorysById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public String getCategory()
  {
    return category;
  }
  /* Code from template attribute_GetUnique */
  public static GameCategory getWithCategory(String aCategory)
  {
    return gamecategorysByCategory.get(aCategory);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithCategory(String aCategory)
  {
    return getWithCategory(aCategory) != null;
  }

  public void delete()
  {
    gamecategorysById.remove(getId());
    gamecategorysByCategory.remove(getCategory());
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "category" + ":" + getCategory()+ "]";
  }
}