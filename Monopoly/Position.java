/**
 * 7.4DN Custom Program (Monopoly)
 * Position
 * An initial attempt to define a position with 
 * it's owner, information, price, rent and type
 * @author Chiu Fan Hui
 * @version 18 May 2019
 */
public class Position {
    /**
     * Type of the square
     * PROPERTIES the square that user can buy
     * START the square that user start playing
     * CHANCE the square that user can draw a chance
     * PUNISHMENT the square that user can draw a punishment
     * REST the square that user can just task a rest
     */
    public enum Type {PROPERTIES, START, CHANCE, PUNISHMENT, REST}
    
    //Instance data
    
    private Player owner; //owner of the position
    private String information; //information of the position
    private int price; //price of the position
    private int rent; //rent of the position
    private Type type; //type of the position
    
    //Constructors

    public Position(Type t) {
        owner = null;
        switch(t) {
            case START: information = "Start";
            break;
            case CHANCE: information = "Chance";
                break;
            case PUNISHMENT: information = "Punishment";
                break;
            default: information = "Rest";
        }   
        price = 0;
        rent = 0;
        type = t;
    }
    
    public Position(String info, int pr, int r, Type t) {
        owner = null;
        information = info;
        price = pr;
        rent = r;
        type = t;
    }
    
    //Getters and modifiers
    
    /** Returns the owner of the position. */
    public Player getOwner(){
        return owner;
    }
    
    /** Set the owner of the position. */
    public void setOwner(Player p){  
        owner = p; 
    }
    
    /** Returns the infomation of the position. */
    public String getInfomation(){
        return information;
    }
    
    /** Set the information of the position. */
    public void setInformation(String info){
        information = info;
    }
    
    /** Returns the price of the position. */
    public int getPrice(){
        return price;
    }
    
    /** Set the price of the position. */
    public void setPrice(int pr){
        price = pr;
    }
    
    /** Returns the rent of the position. */
    public int getRent(){
        return rent;
    }
    
    /** Set the rent of the position. */
    public void setRent(int r){
        rent = r;
    }
    
    /** Returns the type of the position. */
    public Type getType(){
        return type;
    }
    
    /** Set the type of the position. */
    public void setType(Type t){
        type = t;
    }
    
}