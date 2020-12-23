import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * 7.4DN Custom Program (Monopoly)
 * GameManager
 * Allow user to set the number of player, 
 * select size of the board and
 * assign the player's data then play a game
 * @author Chiu Fan Hui
 * @version 18/05/2019
 */
public class GameManager {
    private int numPlayer; //the number of player
    private Position[] square; //square that used during the game
    private boolean gameOver; //whether the player lose
    private Player[] player; //player list
    private Scanner sc; //scan user input
    private final char QUESTION = '?'; //the player ask for their information
    
    public GameManager() {
        sc = new Scanner(System.in);
        gameOver = false;
    }
    
    /** 
     * playerSetting
     * allow user to set the number of player
     */
    public void playerSetting() {
        final int MIN_PLAYER = 2; //minimum number of player
        final int MAX_PLAYER = 4; //maximum number of player
        
        System.out.println("- Monopoly -");
        
        //make sure the number of player is from 2(minPlayer) to 4(maxPlayer)
        do {
            System.out.println("\nHow many player? (" + MIN_PLAYER + " - " + MAX_PLAYER + ")");
            
            try {
                numPlayer = sc.nextInt(); 
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid value");
                //this will empty the buffer
                sc.nextLine();
            }
        } while (numPlayer < MIN_PLAYER || numPlayer > MAX_PLAYER);
        squareSelection();
    }
    
    /** 
     * squareSelection
     * allow user to select size of the board
     */
    private void squareSelection() {
        final int SMALL_BOARD = 1; //select a small board
        final int BIG_BOARD = 2; //select a big board
        final int BOARD_SIZE = 20; //size of the board
        int squareSelection = 0; //user selection of square
        final int START_POS = 0; //position of start
        final int CHANCE_POS = 5; //position of chance
        final int PUNISHMENT_POS = 10; //position of punishment
        final int REST_POS = 15; //position of rest
        int price = 50; //the price of the square
        int rent = price; //the rent of the square
        
        do {
            System.out.println("\nPlease select the size of board (" + SMALL_BOARD + "/" + BIG_BOARD + ")");
            System.out.println(SMALL_BOARD + ".Small");
            System.out.println(BIG_BOARD + ".Big");
            try {
                squareSelection = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid value");
                //this will empty the buffer
                sc.nextLine(); 
            }
        } while (squareSelection != SMALL_BOARD && squareSelection != BIG_BOARD);
        
        square = new Position[BOARD_SIZE * squareSelection]; //size of the square
        
        //set square
        //Start
        square[START_POS * squareSelection] = new Position(Position.Type.START); 
        //Chance
        square[CHANCE_POS * squareSelection] = new Position(Position.Type.CHANCE);
        //Punishment
        square[PUNISHMENT_POS * squareSelection] = new Position(Position.Type.PUNISHMENT); 
        //Rest
        square[REST_POS * squareSelection] = new Position(Position.Type.REST);
        
        for(int i = 1; i < square.length; i++) {
            //skip chance, punishment, rest
            if(i % (5 * squareSelection) != 0) { 
                square[i] = new Position("position " + i, price*(1 + (i/(5 * squareSelection))) , rent*(1 +(i/(5 * squareSelection))), Position.Type.PROPERTIES);
            }
        }
        playerData();
    }
    
    /** 
     * playerData
     * Assign the player's data with
     * name, initial money, initial position,
     * initial number of properties,
     * whether he/she is game over
     */
    private void playerData() {
        int sum = 0; //sum of price of properties
        int iniMoney; //initial money of player
        final int INI_POSITION = 0; //initial position of player
        final int INI_PROPERTIES = 0; //initial number of properties of player
        String name; //name of the player
        
        //calculate the sum of the properties price
        for (int i = 0; i < square.length; i++) {
            sum += square[i].getPrice();
        }
        
        //calculate the initial money of the player
        iniMoney = sum/numPlayer;
        
        //assign the player's data 
        player = new Player[numPlayer]; 
        for (int i = 0; i < numPlayer; i++) {
            System.out.println("\nName of player " + (i + 1) + ": ");
            name = sc.next(); 
            player[i] = new Player(name, iniMoney, INI_POSITION, INI_PROPERTIES, gameOver);
        }
        playGame();
    }
    
    /** 
     * playGame
     * Ask whether the user want to play a game,
     * call other method to check position type,
     * roll a dice, draw a chance and punishment,
     * pay the rent, empty loser properties and show winner
     */
    private void playGame() {
        String startGame; //starting a game
        final String START_GAME_YES = "y"; //the user want to start a game
        
        System.out.println("\nStart the game? (y) or press another key to leave");
        startGame = sc.next();
        if (startGame.equals(START_GAME_YES)) {
            System.out.println("\nEnter (?) for display Information or quit at anytime. There are no further prompts after this point.");
            while (!gameOver(player)) {
                for (int i = 0; i < numPlayer; i++) {
                    if (player[i].getGameOver() == false) {
                        rollDice(player[i]);
                        checkPosType(player[i]);
                    }
                    if (player[i].getMoney() <= 0) {
                        System.out.println(player[i].getName() + ", you lose!");
                        player[i].setNumProperties(0);
                        lostEverything(player[i]);
                        
                        if(gameOver(player)){
                            System.out.println("\n");
                            break;
                        }
                    }
                }
            }
            winner(player);
        } else {
            System.out.println("See you!");
        }
    }
    
    /**
     * checkPosType
     * check the type of the position, then
     * call other method for buying properties,
     * pay rent and read card
     * @param crPlayer the current player
     */
    private void checkPosType(Player crPlayer) {
        
        //check if the player is on properties then call method payRent or buyProperties
        if (square[crPlayer.getPosition()].getType() == Position.Type.PROPERTIES) {
            if (square[crPlayer.getPosition()].getOwner() == null) {
                buyProperties(crPlayer);
            } else if (square[crPlayer.getPosition()].getOwner() == crPlayer) {
                System.out.println(crPlayer.getName() + ", you already bought this property");
            } else {
                payRent(crPlayer);
            }
        }  
        
        //check if the player is on chance or punishment then call method readCard
        else if (square[crPlayer.getPosition()].getType() == Position.Type.CHANCE || square[crPlayer.getPosition()].getType() == Position.Type.PUNISHMENT) {
            drawCard(crPlayer);
        }
        
        //check if the player is on rest then take a rest
        else if (square[crPlayer.getPosition()].getType() == Position.Type.REST) {
            System.out.println("You have played a while, take a rest");
        }
        
    }
    
    /** 
     * payRent
     * Allow player to pay rent,
     * show how much money left of the player,
     * show how much money of the owner of the propertiy has 
     * @param crPlayer the current player
     */
    private void payRent(Player crPlayer) {
        
        System.out.println("\n" + crPlayer.getName() + ", you need to pay rent $" + square[crPlayer.getPosition()].getRent() +" to " + square[crPlayer.getPosition()].getOwner().getName());
        crPlayer.changeMoney(-square[crPlayer.getPosition()].getRent());
        System.out.println(crPlayer.getName() + ", you now have $" + crPlayer.getMoney() + " left.");
        square[crPlayer.getPosition()].getOwner().changeMoney(square[crPlayer.getPosition()].getRent());
        System.out.println(square[crPlayer.getPosition()].getOwner().getName() + ", you now have $" + square[crPlayer.getPosition()].getOwner().getMoney() + ".");
        
    }
    
    /** 
     * buyProperties
     * Show the price of properties,
     * how much money the player has,
     * allow player to buy properties,
     * show how much money left of the player 
     * @param crPlayer the current player
     */
    private void buyProperties(Player crPlayer) {
        char buy; //whether the player want to buy properties
        final char BUY_YES = 'y'; //the player want to buy a property 
        
        if (crPlayer.getMoney() > square[crPlayer.getPosition()].getPrice()) { //check if the player have enough money to buy property
            System.out.println("\nposition " + crPlayer.getPosition() + " cost $" + square[crPlayer.getPosition()].getPrice());
            System.out.println(crPlayer.getName() + ", you have $" + crPlayer.getMoney() + ".");
            do {
                System.out.println(crPlayer.getName() + ", do you want to buy a property? (y) or press other key to pass."); //ask the player whether he want to buy properties
                buy = sc.next().charAt(0);
                if (buy == QUESTION) { 
                    displayOrQuit(crPlayer);
                }
            } while (buy == QUESTION);
            if (buy == BUY_YES) {
                square[crPlayer.getPosition()].setOwner(crPlayer); //assign the position to the player
                crPlayer.changeMoney(-square[crPlayer.getPosition()].getPrice());
                crPlayer.setNumProperties(crPlayer.getNumProperties() + 1);
                System.out.println("\n" + crPlayer.getName() + ", you own position " + crPlayer.getPosition() + " now.");
                System.out.println(crPlayer.getName() + ", you now have $" + crPlayer.getMoney() + " left."); //show player how much money they still have
            } else {
                System.out.println("Pass!");
            }
        } else {
            System.out.println(crPlayer.getName() + ", you don't have enough money to buy this property.");
        }
    }
    
    /** 
     * rollDice
     * Roll a dice,
     * show player current position
     * then calculate the player position
     * @param crPlayer the current player
     */
    private void rollDice(Player crPlayer) {
        Dice dice = new Dice(); //roll a dice
        char roll; //key to roll a dice
        final char ROLL_YES = 'r'; //player want to roll a dice
        
        System.out.println("\n" + crPlayer.getName() + " is in position " + crPlayer.getPosition() + " now."); //show the player current position
        
        do {
            do {
                System.out.println(crPlayer.getName() + ", press (r) to roll a dice");
                roll = sc.next().charAt(0);
                if (roll == QUESTION) { 
                    displayOrQuit(crPlayer);
                }
            } while (roll == QUESTION);
            if (roll == ROLL_YES) {
                dice.roll();
                System.out.println("\n" + crPlayer.getName() + " roll a " + dice.getFaceValue() + "!");
                crPlayer.setPosition((crPlayer.getPosition() + dice.getFaceValue()), square); //calculate the player position after adding the value of side
                System.out.println(crPlayer.getName() + " is in position " + crPlayer.getPosition() + " now.");
            } else {
                System.out.println("Wrong input!\n");
            }
        } while (roll != ROLL_YES);
    }
    
    /** 
     * drawCard
     * draw a card,
     * call method chance or punishment 
     * @param crPlayer the current player
     */
    private void drawCard(Player crPlayer) {
        CardCentre card = new CardCentre();; //draw a card
        char draw; //key to draw a card
        final char DRAW_YES = 'd'; //the playerer draw a chance 
        
        do {
            do {
                System.out.println(crPlayer.getName() + ", press (d) to draw a chance");
                draw = sc.next().charAt(0);
                System.out.println();
                if (draw == QUESTION) { 
                    displayOrQuit(crPlayer);
                }
            } while (draw == QUESTION);
            if (draw == DRAW_YES) {
                if (square[crPlayer.getPosition()].getType() == Position.Type.CHANCE) {
                    chance(crPlayer, card);
                } else {
                    punishment(crPlayer, card); 
                } 
            } else {
                System.out.println("Wrong input!");
            }
        } while (draw != DRAW_YES);
    }
    
    /**
     * Chance
     * check the type of chance card
     * calcualte how much money the player has after getting a chance card or
     * show which position player is in after getting a chance card
     * @param crPlayer the current player
     * @param card draw a card
     */
    private void chance(Player crPlayer, CardCentre card) {
        
        if (card.checkCardAndValue() == CardCentre.CardType.MONEY) {
            System.out.println(crPlayer.getName() + " draw a chance - Get money!");
            System.out.println(crPlayer.getName() + " get $" + card.getMoney() + "!");
            crPlayer.changeMoney(card.getMoney());
            System.out.println(crPlayer.getName() + " now have $" + crPlayer.getMoney() + ".");
        } else {
            System.out.println(crPlayer.getName() + " draw a chance - Step forward!");
            System.out.println(crPlayer.getName() + " can step " + card.getStep() + " step forward.");
            crPlayer.setPosition((crPlayer.getPosition() + card.getStep()), square);
            if (square[crPlayer.getPosition()].getOwner() == null) {
                buyProperties(crPlayer);
            } else if (square[crPlayer.getPosition()].getOwner() != crPlayer) {
                System.out.println("You don't need to pay rent to " + square[crPlayer.getPosition()].getOwner().getName() + "!");
            }
            System.out.println(crPlayer.getName() + " now in position " + crPlayer.getPosition());
        }
        
    }
    
    /**
     * Punishment
     * check the type of punishment card
     * calcualte how much money the player has after getting a punishment card or
     * show which position player is in after getting a punishment card
     * @param crPlayer the current player
     * @param card draw a card
     */
    private void punishment(Player crPlayer, CardCentre card) {
        
        if (card.checkCardAndValue() == CardCentre.CardType.MONEY) {
            System.out.println(crPlayer.getName() + " draw a punishment - Pay tax!"); 
            System.out.println(crPlayer.getName() + " need to pay $" + card.getMoney() + " tax."); 
            crPlayer.changeMoney(-card.getMoney());
            System.out.println(crPlayer.getName() + " now have $" + crPlayer.getMoney() + ".");
        } else {
            System.out.println(crPlayer.getName() + " draw a punishment - Step backward!"); 
            System.out.println(crPlayer.getName() + " need to step " + card.getStep() + " step backward."); 
            crPlayer.setPosition((crPlayer.getPosition() - card.getStep()), square);
            if (square[crPlayer.getPosition()].getOwner() == null) {
                System.out.println("You can't buy property now!");
            } else if (square[crPlayer.getPosition()].getOwner() != crPlayer) {
                payRent(crPlayer);
            }
            System.out.println(crPlayer.getName() + " now in position " + crPlayer.getPosition());
        } 
        
    }
    
    /** 
     * gameOver
     * Check how many player are lose
     * @param playerArray player list
     */
    private boolean gameOver(Player[] playerArray) {
        int numLoser = 0; //number of loser
        
        for (int i = 0; i < numPlayer; i++) {     
            if (playerArray[i].getGameOver()) {
                numLoser++;
            }
        }
        if (numLoser == numPlayer - 1) {
            return true;
        } else {
            return false;
        }
    }
    
    /** 
     * displayOrQuit
     * Display a menu that
     * can show information of the player and 
     * quit the game
     * @param player the current player
     */
    private void displayOrQuit(Player crPlayer) {
        int displaySelection = 0; //selection of diplay
        final int SHOW_INFO = 1; //show information of the player
        final int QUIT = 2; //quit the game
        
        do {
            System.out.println("\nPlease select (" + SHOW_INFO + "/" + QUIT + ")");
            System.out.println(SHOW_INFO + ". Show your Information");
            System.out.println(QUIT + ". Quit");
            try {
                displaySelection = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid value");
                sc.nextLine(); //this will empty the buffer
            }
            switch(displaySelection) {
                case SHOW_INFO: System.out.println("\n" + crPlayer);
                break;
                case QUIT: System.out.println("See you!");
                System.exit(0);
            }
        } while (displaySelection != SHOW_INFO && displaySelection != QUIT);
    }
    
    /** 
     * winner
     * Show which player is winner
     * @param playerArray player list
     */
    private void winner(Player[] playerArray){
        String winner = ""; //name of winner
        
        for (int i = 0; i < numPlayer; i++) {
            if (playerArray[i].getMoney() > 0) {
                winner = playerArray[i].getName();
            }
        }
        System.out.println("Winner is " + winner);
    }
    
    /** 
     * lostEverything
     * If the player is lose,
     * all of his/her properties will be empty
     * @param crPlayer the current player
     */
    private void lostEverything(Player crPlayer) {
        
        for(int i = 0; i < square.length; i++) {
            if(square[i].getOwner() == crPlayer)
                square[i].setOwner(null);
        }
    }
    
    /** 
     * continuePlay
     * Ask whether the user want to play again
     * @return true
     * @return false
     */
    public boolean continuePlay() {
        char playAgain; //whether the user want to play again
        final char PLAY_AGAIN_YES = 'y'; //the user want to play again
        
        System.out.println("\nPlay again? (y) or press another key to leave");
        playAgain = sc.next().charAt(0);
        if (playAgain == PLAY_AGAIN_YES) {
            return true;
        } else {
            System.out.println("See you!");
            return false;
        }
    }
    
}

