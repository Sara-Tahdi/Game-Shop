/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;

// line 84 "../../../../../../GameCenter.ump"
public class GameRequest extends Request
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Type { ADD, REMOVE }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameRequest Attributes
  private Type type;

  //GameRequest Associations
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameRequest(Status aStatus, Staff aCreatedRequest, Type aType, Game aGame)
  {
    super(aStatus, aCreatedRequest);
    type = aType;
    if (!setGame(aGame))
    {
      throw new RuntimeException("Unable to create GameRequest due to aGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setType(Type aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public Type getType()
  {
    return type;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setGame(Game aNewGame)
  {
    boolean wasSet = false;
    if (aNewGame != null)
    {
      game = aNewGame;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    game = null;
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "type" + "=" + (getType() != null ? !getType().equals(this)  ? getType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}