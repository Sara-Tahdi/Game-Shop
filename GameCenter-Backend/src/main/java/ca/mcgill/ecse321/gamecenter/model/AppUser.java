/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.*;

import java.util.*;

// line 10 "../../../../../../GameCenter.ump"
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "appUsers")
@DiscriminatorColumn(name = "USER_TYPE")
public abstract class AppUser
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, AppUser> appusersById = new HashMap<Integer, AppUser>();
  private static Map<String, AppUser> appusersByEmail = new HashMap<String, AppUser>();
  private static Map<String, AppUser> appusersByUsername = new HashMap<String, AppUser>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //AppUser Attributes
  @Id
  @GeneratedValue
  private int id;
  private String email;
  private String username;
  private String password;
  private boolean isActive;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public AppUser() {
    isActive = true;
  }
  public AppUser(String aEmail, String aUsername, String aPassword)
  {
    password = aPassword;
    isActive = true;
    if (!setEmail(aEmail))
    {
      throw new RuntimeException("Cannot create due to duplicate email. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setUsername(aUsername))
    {
      throw new RuntimeException("Cannot create due to duplicate username. See https://manual.umple.org?RE003ViolationofUniqueness.html");
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
      appusersById.remove(anOldId);
    }
    appusersById.put(aId, this);
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    String anOldEmail = getEmail();
    if (anOldEmail != null && anOldEmail.equals(aEmail)) {
      return true;
    }
    if (hasWithEmail(aEmail)) {
      return wasSet;
    }
    email = aEmail;
    wasSet = true;
    if (anOldEmail != null) {
      appusersByEmail.remove(anOldEmail);
    }
    appusersByEmail.put(aEmail, this);
    return wasSet;
  }

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    String anOldUsername = getUsername();
    if (anOldUsername != null && anOldUsername.equals(aUsername)) {
      return true;
    }
    if (hasWithUsername(aUsername)) {
      return wasSet;
    }
    username = aUsername;
    wasSet = true;
    if (anOldUsername != null) {
      appusersByUsername.remove(anOldUsername);
    }
    appusersByUsername.put(aUsername, this);
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsActive(boolean aIsActive)
  {
    boolean wasSet = false;
    isActive = aIsActive;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static AppUser getWithId(int aId)
  {
    return appusersById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public String getEmail()
  {
    return email;
  }
  /* Code from template attribute_GetUnique */
  public static AppUser getWithEmail(String aEmail)
  {
    return appusersByEmail.get(aEmail);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithEmail(String aEmail)
  {
    return getWithEmail(aEmail) != null;
  }

  public String getUsername()
  {
    return username;
  }
  /* Code from template attribute_GetUnique */
  public static AppUser getWithUsername(String aUsername)
  {
    return appusersByUsername.get(aUsername);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithUsername(String aUsername)
  {
    return getWithUsername(aUsername) != null;
  }

  public String getPassword()
  {
    return password;
  }

  public boolean getIsActive()
  {
    return isActive;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsActive()
  {
    return isActive;
  }

  public void delete()
  {
    appusersById.remove(getId());
    appusersByEmail.remove(getEmail());
    appusersByUsername.remove(getUsername());
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "email" + ":" + getEmail()+ "," +
            "username" + ":" + getUsername()+ "," +
            "password" + ":" + getPassword()+ "," +
            "isActive" + ":" + getIsActive()+ "]";
  }
}