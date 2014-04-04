/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import java.util.ArrayList;

/**
 *
 * @author yakir
 */
public interface Communicable 
{

    public void PrintAllPlayers(ArrayList<Player> GamePlayers);

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
        SAVE_GAME("Save Game"), 
        CONTINUE_GAME("Continue Game"), 
        EXIT_GAME("Exit Game"); 
        
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
    
    public boolean DoesPlayerContinue(Player player);
    public String getFilePathForSave();
    public PlayerAction GetWantedAction();
    public void PrintPlayerInfo(Player PlayerToPrint);
    public void PrintBasicPlayerInfo(Player PlayerToPrint);
    public RoundAction GetFinishRoundAction();
    public Double GetBidForPlayer(Player BettingPlayer);    
    public void PrintBidInfo(Bid BidForPrint, Player PlayerBid);
    public void PrintHandInfo(Hand HandForPrint,GameParticipant ParPlayer);
    public void PrintParticipnatName(GameParticipant PartToPrint);
    public void PrintMessage(String Message);
    
}
