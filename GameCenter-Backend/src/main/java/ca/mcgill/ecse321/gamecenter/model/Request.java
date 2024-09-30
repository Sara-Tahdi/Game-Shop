/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse321.gamecenter.model;

// line 76 "../../../../../../GameCenter.ump"
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
  private Status status;

  //Request Associations
  private Staff createdRequest;

  //------------------------
  // CONSTRUCTOR
  //------------------------

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

  public boolean setStatus(Status aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
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