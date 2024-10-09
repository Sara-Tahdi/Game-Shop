/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.*;

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
    email = aEmail;
    username = aUsername;
    password = aPassword;
    isActive = true;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void setId(int aId)
  {
    id = aId;
  }

  public void setEmail(String aEmail)
  {
    email = aEmail;
  }

  public void setUsername(String aUsername)
  {
    username = aUsername;
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

  public String getEmail()
  {
    return email;
  }

  public String getUsername()
  {
    return username;
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