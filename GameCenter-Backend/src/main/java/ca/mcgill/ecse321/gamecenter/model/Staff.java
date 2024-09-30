/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import java.util.*;

// line 19 "../../../../../../GameCenter.ump"
public abstract class Staff extends AppUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Staff(int aId, String aEmail, String aUsername, String aPassword, boolean aIsActive)
  {
    super(aId, aEmail, aUsername, aPassword, aIsActive);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}