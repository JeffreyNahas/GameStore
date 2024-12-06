/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.videogamessystem.model;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;


// line 41 "model.ump"
// line 138 "model.ump"
@Entity
public class SpecificGame
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SpecificGame Attributes
  @Id
  private int serialNumber;

  private boolean availability;

  //SpecificGame Associations

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Game game;
  @ManyToOne
  private SpecificOrder specificOrder;

  //------------------------
  // CONSTRUCTOR
  

  public SpecificGame(int aSerialNumber, boolean aAvailability){
    serialNumber = aSerialNumber;
    availability = aAvailability;
  }

  public SpecificGame(){
    
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSerialNumber(int aSerialNumber)
  {
    boolean wasSet = false;
    serialNumber = aSerialNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvailability(boolean aAvailability)
  {
    boolean wasSet = false;
    availability = aAvailability;
    wasSet = true;
    return wasSet;
  }

  public int getId() {
    return serialNumber;
  }
  //also gets id
  public int getSerialNumber()
  {
    return serialNumber;
  }

  public boolean getAvailability()
  {
    return availability;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public SpecificOrder getSpecificOrder()
  {
    return specificOrder;
  }

  public boolean hasOrder()
  {
    boolean has = specificOrder != null;
    return has;
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
  /* Code from template association_SetOptionalOneToMandatoryMany */
  public boolean setSpecificOrder(SpecificOrder aSpecificOrder)
  {
    boolean wasSet = false;
    if (aSpecificOrder != null) {
      specificOrder = aSpecificOrder;
      wasSet = true;
    }
    return wasSet;
  }
  
  public void delete()
  {
    game = null;
    specificOrder = null;
  }

  
  public String toString() {
    return super.toString() + "[" +
        "serialNumber" + ":" + getSerialNumber() + "," +
        "  " + "availability" + "=" + getAvailability() + System.getProperties().getProperty("line.separator") +
        "  " + "game = " + (getGame() != null ? Integer.toHexString(System.identityHashCode(getGame())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "order = " + (getSpecificOrder() != null ? Integer.toHexString(System.identityHashCode(getSpecificOrder())) : "null");
  }
}