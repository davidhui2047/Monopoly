/*
 * 7.4DN Custom Program
 * Monopoly
 * run a monopoly
 * @author Chiu Fan Hui
 * @version 18/05/2019
 */
public class Monopoly {
    
    public static void main(String[] args) {
        GameManager monopoly; //a new monopoly
        
        do {
            monopoly = new GameManager();
            monopoly.playerSetting();
        } while (monopoly.continuePlay());
    } 
    
}