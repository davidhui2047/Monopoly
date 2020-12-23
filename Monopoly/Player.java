/**
 * 7.4DN Custom Program (Monopoly)
 * Player
 * An initial attempt to define a position with
 * his/her name, money, position, number of properties
 * properties and whether he/she is game over
 * @author Chiu Fan Hui
 * @version 18 May 2019
 */
public class Player {
    //Instance data
    
    private String name; //name of the player
    private int money; //how much money does the player have
    private int position; //which position the player stand on
    private int numProperties; //the number of properties that player has
    private boolean gameOver; //whether the player is lose
    String information; //display the information of the player
    
    //Constructors
    
    public Player (String n, int m, int pst, int nppt, boolean g) {
        name = n;
        money = m;
        position = pst;
        numProperties = nppt;
        gameOver = g;
    }
    
    //Getters and modifiers
    
    /** Returns the name of the player. */
    public String getName(){
        return name;
    }
    
    /** Set the name of the player. */
    public void setName(String n){
        name = n;
    }
    
    /** Returns the money of the player. */
    public int getMoney(){
        return money;
    }
    
    /** Set the money of the player. */
    public void setMoney(int m){
        money = m;
    }
    
    /** Returns the position of the player. */
    public int getPosition(){
        return position;
    }
    
    /** Set the position of the player. */
    public void setPosition(int pst, Position[] square){
        if (pst >= square.length) {
            pst -= square.length;
            position = pst;
        } else {
            position = pst;
        }
    }
    
    /** Returns the number of properties of the player. */
    public int getNumProperties(){
        return numProperties;
    }
    
    /** Set the number of properties of the player. */
    public void setNumProperties(int nppt){
        numProperties = nppt;
    }
    
    /** Returns whether the player is game over. */
    public boolean getGameOver(){
        return gameOver;
    }
    
    /** Set whether the player is game over. */
    public void setGameOver(boolean g){
        gameOver = g;
    }

    /** change the money of the player and if it is less than 0 then the player will lose. */
    public void changeMoney(int m){
        money += m;
        if (money <= 0) {
            gameOver = true;
        }
    }
    
    /** Returns the information of the player. */
    public String toString() {
        
        String information = name + ": have $" + money + ", in position " + position + ", have " + numProperties + " properties.\n";
        return information;
    }
    
}


