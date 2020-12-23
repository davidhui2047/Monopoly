import java.util.Random;

/**
 * 7.4DN Custom Program (Monopoly)
 * Dice
 * Roll the dice
 * @author Chiu Fan Hui
 * @version 18 May 2019
 */
public class Dice {
    //Instance data
    
    final private int NUM_FACES; //number of sides
    private int faceValue; //current side of dice
    private Random generator; //to generate a face value randomly
    
    //Constructors
    
    public Dice() {
        NUM_FACES = 6;
        faceValue = 0;
        generator = new Random();
    }
    
    /** Returns the current face value of dice. */
    public int getFaceValue() {
        return faceValue;
    }
    
    /** Roll the dice and return the value of current face. */
    public int roll() {
        faceValue = generator.nextInt(NUM_FACES) + 1;
        return faceValue;
    }
    
}