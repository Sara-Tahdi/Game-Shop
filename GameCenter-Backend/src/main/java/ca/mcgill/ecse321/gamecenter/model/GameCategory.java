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

  public GameCategory() {}

  public GameCategory(String aCategory)
  {
    category = aCategory;
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

  public boolean setCategory(String aCategory)
  {
    boolean wasSet = false;
    category = aCategory;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public String getCategory()
  {
    return category;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "category" + ":" + getCategory()+ "]";
  }
}