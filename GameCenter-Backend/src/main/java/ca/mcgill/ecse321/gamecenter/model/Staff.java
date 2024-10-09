/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

// line 19 "../../../../../../GameCenter.ump"
@Entity
@DiscriminatorValue("STAFF")
public abstract class Staff extends AppUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Staff() {
    super();
  }

  public Staff(String aEmail, String aUsername, String aPassword)
  {
    super(aEmail, aUsername, aPassword);
  }

  //------------------------
  // INTERFACE
  //------------------------

}