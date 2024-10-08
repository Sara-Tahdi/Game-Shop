package ca.mcgill.ecse321.gamecenter.model;
import java.util.*;
import jakarta.persistence.*;

// line 76 "../../../../../../GameCenter.ump"
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "requests")
@DiscriminatorColumn(name = "REQUEST_TYPE")
public abstract class Request
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Status { PENDING, APPROVED, DENIED }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Request Attributes
  @Id
  @GeneratedValue
  private int id;
  private Status status;

  //Request Associations
  @OneToOne
  private Staff createdRequest;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Request() {}

  public Request(Status aStatus, Staff aCreatedRequest)
  {
    status = aStatus;
    if (!setCreatedRequest(aCreatedRequest))
    {
      throw new RuntimeException("Unable to create Request due to aCreatedRequest. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(Status aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }

  public Status getStatus()
  {
    return status;
  }
  /* Code from template association_GetOne */
  public Staff getCreatedRequest()
  {
    return createdRequest;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCreatedRequest(Staff aNewCreatedRequest)
  {
    boolean wasSet = false;
    if (aNewCreatedRequest != null)
    {
      createdRequest = aNewCreatedRequest;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    createdRequest = null;
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "createdRequest = "+(getCreatedRequest()!=null?Integer.toHexString(System.identityHashCode(getCreatedRequest())):"null");
  }
}