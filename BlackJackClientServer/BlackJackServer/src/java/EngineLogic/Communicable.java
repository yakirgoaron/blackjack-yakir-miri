/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Exception.PlayerResigned;
import java.util.ArrayList;

/**
 *
 * @author yakir
 */
public interface Communicable 
{

    public void PrintAllPlayers(ArrayList<Player> GamePlayers);

    public void RemovePlayer(Player player);

   // public void PrintPlayerMessage(GameParticipant ParPlayer, String Message);
    
    public void PrintPlayerAction(AIPlayer PlayerAct, PlayerAction Action);
    
    public void GameWinner(Player PlayerWin);

    public static enum PlayerAction{
        
        DOUBLE("Double"), HIT("Hit"), SPLIT("Split"), STAND("Stand");
        
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
    
    public void DoesPlayerContinue(Player player);
    public String GetFilePathForSave();
    public PlayerAction GetWantedAction(Player plyerDoesAction) throws PlayerResigned;
    public void PrintPlayerInfo(Player PlayerToPrint);
    public void PrintBasicPlayerInfo(Player PlayerToPrint);
    public RoundAction GetFinishRoundAction() throws PlayerResigned;
    public Double GetBidForPlayer(Player BettingPlayer) throws PlayerResigned;    
    public void PrintBidInfo(Bid BidForPrint, Player PlayerBid);
    public void PrintHandInfo(Hand HandForPrint,GameParticipant ParPlayer);
    public void PrintMessage(String Message);
    public void GameEnded();
    
}
