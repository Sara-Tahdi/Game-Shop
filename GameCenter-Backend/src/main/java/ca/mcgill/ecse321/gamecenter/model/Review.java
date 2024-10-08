/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.*;

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
  private int thumbsUp;
  private int thumbsDown;

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
    thumbsUp = aThumbsUp;
    thumbsDown = aThumbsDown;
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

  public boolean setThumbsUp(int aThumbsUp)
  {
    boolean wasSet = false;
    thumbsUp = aThumbsUp;
    wasSet = true;
    return wasSet;
  }

  public boolean setThumbsDown(int aThumbsDown)
  {
    boolean wasSet = false;
    thumbsDown = aThumbsDown;
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

  public int getThumbsUp()
  {
    return thumbsUp;
  }

  public int getThumbsDown()
  {
    return thumbsDown;
  }

  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "author" + ":" + getAuthor()+ "," +
            "review" + ":" + getReview()+ "," +
            "managerReply" + ":" + getManagerReply()+ "," +
            "thumbsUp" + ":" + getThumbsUp()+ "," +
            "thumbsDown" + ":" + getThumbsDown()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "stars" + "=" + (getStars() != null ? !getStars().equals(this)  ? getStars().toString().replaceAll("  ","    ") : "this" : "null");
  }
}