import java.util.Random;

/**
 * 7.4DN Custom Program (Monopoly)
 * Card centre
 * the centre that put different type of card together
 * and draw a card
 * @author Chiu Fan Hui
 * @version 30 May 2019
 */
public class CardCentre { 
    /**
     * Different types of card
     * MONEY will increase or minus the money of player 
     * STEP player can step forward or step backword
     */
    public enum CardType {MONEY, STEP}
    
    //Instance data
    
    private int[][] cardList; //card list
    private int card; //what card was drawed
    private int money; //how much money the player will have
    private int step; //how mamny step the player need to move
    private int drawCard; //drew a card
    private final int MONEY_LIST_POS = 0; //position of money list
    private Random drawer; //draw a chance randomly
    
    //Constructors
    
    public CardCentre() {
        final int CAPACITY = 4; //capacity of money and step list
        final int LOWEST_MONEY = 50; //lowest money in money list
        final int SMALLEST_STEP = 2; //smallest step in step list
        int[] moneyList = new int[CAPACITY]; //money list that provide a value of money to draw
        int[] stepList = new int[CAPACITY]; //step list that provide a value of step to draw
        final int STEP_LIST_POS = 1; //position of step list
        
        //set the value of money and step list
        for (int i = 0; i < CAPACITY; i++) {
            for (int j = 1; j <= CAPACITY; j++) {
                moneyList[i] = LOWEST_MONEY*j;
                stepList[i] = SMALLEST_STEP*j;
            }
        }
        
        cardList = new int [CardType.values().length][moneyList.length];
        cardList[MONEY_LIST_POS] = moneyList;
        cardList[STEP_LIST_POS] = stepList;
        drawer = new Random();
        drawCard = drawer.nextInt(cardList.length); 
        int drawValue = drawer.nextInt(cardList[MONEY_LIST_POS].length); //drew a value of the card
        card = cardList[drawCard][drawValue];
    }
    
    /**
     * checkCardAndValue
     * check the card and assign the value in appropriate variable
     * @return CardCentre.Type.MONEY money type of the card
     * @return CardCentre.Type.MONEY step type of the card
     */
    public CardType checkCardAndValue() {
        switch(drawCard) {
            case MONEY_LIST_POS: 
                money = card; 
                return CardCentre.CardType.MONEY;
            default: 
                step = card; 
                return CardCentre.CardType.STEP;
        }
    }
    
    /** Returns the value of money. */
    public int getMoney() {
        return money;
    }
    
    /** Returns the value of step. */
    public int getStep() {
        return step;
    }
    
}
