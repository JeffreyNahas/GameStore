/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.videogamessystem.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

// line 96 "model.ump"
// line 153 "model.ump"
@Entity
@DiscriminatorValue("STAFF")
public class Staff extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Staff Attributes

  private boolean admin;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  
  public Staff() {
      super();
  }

  public Staff(String aUserName, String aEmail, String aPassword, boolean isAdmin)
  {
    super(aUserName, aEmail, aPassword);
    admin = isAdmin;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Long getId() {
    return super.getId();
  }

  public boolean setStaffType(boolean isAdmin)
  {
    boolean wasSet = false;
    admin = isAdmin;
    wasSet = true;
    return wasSet;
  }

  public boolean getStaffType()
  {
    return admin;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
      return super.toString() + System.getProperties().getProperty("line.separator") +
              "  " + "admin=" + admin;
  }
}