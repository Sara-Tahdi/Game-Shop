package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.*;

// line 84 "../../../../../../GameCenter.ump"
@Entity
@DiscriminatorValue("GAME_REQUEST")
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
  private String reason;

  //GameRequest Associations
  @OneToOne 
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public GameRequest()
  {
    super();
  }

  public GameRequest(Status aStatus, Staff aCreatedRequest, Type aType, String aReason, Game aGame)
  {
    super(aStatus, aCreatedRequest);
    type = aType;
    reason = aReason;
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

  public boolean setReason(String aReason) {
    boolean wasSet = false;
    reason = aReason;
    wasSet = true;
    return wasSet;
  }

  public Type getType()
  {
    return type;
  }
  public String getReason() { return reason; }
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


  @SuppressWarnings("unlikely-arg-type")
  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "type" + "=" + (getType() != null ? !getType().equals(this)  ? getType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null");
  }
}