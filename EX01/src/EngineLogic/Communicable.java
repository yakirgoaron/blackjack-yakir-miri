/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

/**
 *
 * @author yakir
 */
public interface Communicable 
{
    public static enum PlayerAction{
        
        DOUBLE("Double"), HIT("Hit"), SPLIT("Split"), STAY("Stay");
        
        private final String Description;
        private static final int Size = PlayerAction.values().length;
        
        PlayerAction(String Description){
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }

        public static int getSize() {
            return Size;
        }       
    }
    public static enum RoundAction{      
        SAVE_GAME("Save and Exit game"), 
        NEW_ROUND("New Round"); 
        
        private final String Description;
        private static final int Size = PlayerAction.values().length;
        
        RoundAction(String Description){
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }

        public static int getSize() {
            return Size;
        }       
    }
    
    public PlayerAction GetWantedAction();
    
    public void PrintPlayerInfo(Player PlayerToPrint);
    
    public void PrintBasicPlayerInfo(Player PlayerToPrint);
    
    public RoundAction GetFinishRoundAction();
    
    public Double GetBidForPlayer(Player BettingPlayer);
    
    public void PrintBidInfo(Bid BidForPrint);
    public void PrintHandInfo(Hand HandForPrint);
    
    public void PrintMessage(String Message);
}
