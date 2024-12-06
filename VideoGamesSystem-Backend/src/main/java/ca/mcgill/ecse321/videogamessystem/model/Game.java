/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/
package ca.mcgill.ecse321.videogamessystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

// line 19 "model.ump"
// line 188 "model.ump"
@Entity
public class Game
{
  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String description;
  private int price;
  private String title;
  private Category category;
  private ConsoleType consoleType;

  //Game Associations
  @ManyToOne
  private Wishlist wishlist;
  @ManyToOne
  private Promotion promotion;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aDescription, int aPrice, String aTitle, Category aCategory, 
      ConsoleType aConsoleType)
  {
    description = aDescription;
    price = aPrice;
    title = aTitle;
    category = aCategory;
    consoleType = aConsoleType;

    // //Wishlist aWishlist, GameConsole aGameConsole, Promotion aPromotion
    // Wishlist wishlist = new Wishlist();
    // this.wishlist = wishlist;
    // Promotion promotion = new Promotion();
    // this.promotion = promotion;
  }
  
  public Game(){
    
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }


  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
    wasSet = true;
    return wasSet;
  }

  public boolean setCategory(Category category2)
  {
    boolean wasSet = false;
    category = category2;
    wasSet = true;
    return wasSet;
  }

  public boolean setConsoleType(ConsoleType consoleType) {
    boolean wasSet = false;
    this.consoleType = consoleType;
    wasSet = true;
    return wasSet;
  }

  public Long getId(){
    return id;
  }
  public String getDescription()
  {
    return description;
  }

  public int getPrice()
  {
    return price;
  }

  public String getTitle()
  {
    return title;
  }

  public Category getCategory()
  {
    return category;
  }

  public ConsoleType getConsoleType()
  {
    return consoleType;
  }

  /* Code from template association_GetOne */
  public Wishlist getWishlist()
  {
    return wishlist;
  }

  /* Code from template association_GetOne */
  public Promotion getPromotion()
  {
    return promotion;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setWishlist(Wishlist aNewWishlist)
  {
    boolean wasSet = false;
    if (aNewWishlist != null)
    {
      wishlist = aNewWishlist;
      wasSet = true;
    }
    return wasSet;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setPromotion(Promotion aNewPromotion)
  {
    boolean wasSet = false;
    if (aNewPromotion != null)
    {
      promotion = aNewPromotion;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    wishlist = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "description" + ":" + getDescription()+ "," +
            "price" + ":" + getPrice()+ "," +
            "title" + ":" + getTitle()+ "," +
            "category" + ":" + getCategory()+ 
            "consoleType" + ":" + getConsoleType()+
            "]" + System.getProperties().getProperty("line.separator") +
            "  " + "wishlist = "+(getWishlist()!=null?Integer.toHexString(System.identityHashCode(getWishlist())):"null") + System.getProperties().getProperty("line.separator");
  }

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------
  public enum Category {
    Adventure,
    Action,
    Sports,
    Strategy,
    Puzzle,
    Party,
    Survival,
    Arcade,
    Other;
    
    @Override
    public String toString() {
      return name();
    }
  }
    
  public enum ConsoleType {
    PS4,
    XBOX,
    Switch,
    Wii,
    PC,
    Other;

    @Override
    public String toString() {
      return name();
    }
  }
  
}