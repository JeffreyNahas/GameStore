/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.videogamessystem.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorType;

/**
 * Unable to update umple code due to error at [26,2]
 */
// line 3 "model.ump"
// line 122 "model.ump"
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Change to SINGLE_TABLE instead of JOINED
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Account Attributes
  @Id
  @GeneratedValue(generator = "account_id_sequence_generator", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "account_id_sequence_generator", sequenceName = "account_id_seq", allocationSize = 1)
  private Long id;

  @Column(unique= true)
  private String userName;

  @Column(unique= true)
  private String email;
  
  private String password;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Account(String aUserName, String aEmail, String aPassword)
  {
    userName = aUserName;
    email = aEmail;
    password = aPassword;
  }

  public Account(){
    
  }
  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUserName(String aUserName)
  {
    boolean wasSet = false;
    userName = aUserName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }


  public Long getId(){
    return id;
  }
  
  public String getUserName()
  {
    return userName;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "userName" + ":" + getUserName()+ "," +
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "]";
  }
}