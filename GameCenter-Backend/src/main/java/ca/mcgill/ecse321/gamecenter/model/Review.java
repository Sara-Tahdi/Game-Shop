/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Review() {}

  public Review(String aAuthor, String aReview, String aManagerReply, Rating aStars, int aThumbsUp, int aThumbsDown)
  {
    author = aAuthor;
    review = aReview;
    managerReply = aManagerReply;
    stars = aStars;
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