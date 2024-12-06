/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.videogamessystem.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


// line 76 "model.ump"
// line 163 "model.ump"
@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Attributes

  private int phoneNumber;
  private String address;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(String aUserName, String aEmail, String aPassword, int aPhoneNumber, String aAddress)
  {
    super(aUserName, aEmail, aPassword);
    phoneNumber = aPhoneNumber;
    address = aAddress;
  }

  public Customer(){
    super();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPhoneNumber(int aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public Long getId(){
    return super.getId();
  }

  public int getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getAddress()
  {
    return address;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "address" + ":" + getAddress()+ "]" + System.getProperties().getProperty("line.separator");
  }
}