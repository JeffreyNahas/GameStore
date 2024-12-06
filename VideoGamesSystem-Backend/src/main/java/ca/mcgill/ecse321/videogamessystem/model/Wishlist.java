/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.videogamessystem.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

// line 107 "model.ump"
// line 197 "model.ump"
@Entity
public class Wishlist
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wishlist Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int nbOfItems;

  //Wishlist Associations
  @OneToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wishlist()
  {
    nbOfItems = 0;
    // Customer customer = new Customer();
    // this.customer = customer;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNbOfItems(int aNbOfItems)
  {
    boolean wasSet = false;
    nbOfItems = aNbOfItems;
    wasSet = true;
    return wasSet;
  }

  public boolean setCustomer(Customer customer){
    boolean wasSet= false;
    this.customer= customer;
    wasSet= true;
    return wasSet;
  }

  public Long getId(){
    return id;
  }

  public int getNbOfItems()
  {
    return nbOfItems;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }

  public void delete()
  {
    Customer existingCustomer = customer;
    customer = null;
    if (existingCustomer != null)
    {
      existingCustomer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "nbOfItems" + ":" + getNbOfItems()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}