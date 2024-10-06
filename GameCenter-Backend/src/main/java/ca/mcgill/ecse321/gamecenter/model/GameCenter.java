package ca.mcgill.ecse321.gamecenter.model;
import java.sql.Time;

// line 3 "../../../../../../GameCenter.ump"
public class GameCenter
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //GameCenter Attributes
  private String name;
  private Time open;
  private Time close;
  private String storePolicy;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public GameCenter(String aName, Time aOpen, Time aClose, String aStorePolicy)
  {
    name = aName;
    open = aOpen;
    close = aClose;
    storePolicy = aStorePolicy;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setOpen(Time aOpen)
  {
    boolean wasSet = false;
    open = aOpen;
    wasSet = true;
    return wasSet;
  }

  public boolean setClose(Time aClose)
  {
    boolean wasSet = false;
    close = aClose;
    wasSet = true;
    return wasSet;
  }

  public boolean setStorePolicy(String aStorePolicy)
  {
    boolean wasSet = false;
    storePolicy = aStorePolicy;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public Time getOpen()
  {
    return open;
  }

  public Time getClose()
  {
    return close;
  }

  public String getStorePolicy()
  {
    return storePolicy;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "storePolicy" + ":" + getStorePolicy()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "open" + "=" + (getOpen() != null ? !getOpen().equals(this)  ? getOpen().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "close" + "=" + (getClose() != null ? !getClose().equals(this)  ? getClose().toString().replaceAll("  ","    ") : "this" : "null");
  }
}