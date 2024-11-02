/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// line 109 "../../../../../../GameCenter.ump"\

@Entity
public class Review
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Rating { ONE, TWO, THREE, FOUR, FIVE }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Review Attributes
  @Id
  @GeneratedValue
  private int id;
  private String author;
  private String review;
  private String managerReply;
  private Rating stars;

  //Review Associations
  @ManyToOne
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review() {}

  public Review(String aAuthor, String aReview, String aManagerReply, Rating aStars, int aThumbsUp, int aThumbsDown, Game aGame)
  {
    author = aAuthor;
    review = aReview;
    managerReply = aManagerReply;
    stars = aStars;
    if (!setGame(aGame))
    {
      throw new RuntimeException("Unable to create Review due to aGame. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setAuthor(String aAuthor)
  {
    boolean wasSet = false;
    author = aAuthor;
    wasSet = true;
    return wasSet;
  }

  public boolean setReview(String aReview)
  {
    boolean wasSet = false;
    review = aReview;
    wasSet = true;
    return wasSet;
  }

  public boolean setManagerReply(String aManagerReply)
  {
    boolean wasSet = false;
    managerReply = aManagerReply;
    wasSet = true;
    return wasSet;
  }

  public boolean setStars(Rating aStars)
  {
    boolean wasSet = false;
    stars = aStars;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public String getAuthor()
  {
    return author;
  }

  public String getReview()
  {
    return review;
  }

  public String getManagerReply()
  {
    return managerReply;
  }

  public Rating getStars()
  {
    return stars;
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
  }


  @SuppressWarnings("unlikely-arg-type")
  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "author" + ":" + getAuthor()+ "," +
            "review" + ":" + getReview()+ "," +
            "managerReply" + ":" + getManagerReply()+ "]" +
            System.getProperties().getProperty("line.separator") +
            "  " + "stars" + "=" + (getStars() != null ? !getStars().equals(this)  ? getStars().toString().replaceAll("  ","    ") : "this" : "null");
  }
}