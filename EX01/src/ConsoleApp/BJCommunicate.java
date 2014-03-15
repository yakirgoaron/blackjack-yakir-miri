/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsoleApp;

import ConsoleApp.UserOptions.SaveOptions;
import EngineLogic.Bid;
import EngineLogic.Card;
import EngineLogic.Communicable;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.Player;
import java.util.List;

/**
 *
 * @author miri
 */
public class BJCommunicate implements Communicable{

    String FilePathForSave;

    
    public String getFilePathForSave() {
        return FilePathForSave;
    }
    
    @Override
    public PlayerAction GetWantedAction() {
        MenuMessages.PlayerActionMessage();
        int IntUserChoice = 
                UserOptions.UserIntChoice(PlayerAction.getSize());
        PlayerAction EnumPlayerAction = 
                PlayerAction.values()[IntUserChoice];
         
      return EnumPlayerAction;
    }

    @Override
    public void PrintPlayerInfo(Player PlayerToPrint) {
        
        PrintBasicPlayerInfo(PlayerToPrint);
        List<Bid> playerBids = PlayerToPrint.getBids();        
        System.out.println("player " + PlayerToPrint.getName() + " bids: ");
        
        for (Bid bid : playerBids)
            PrintBidInfo(bid);
    }

    @Override
    public void PrintBasicPlayerInfo(Player PlayerToPrint) {
        System.out.println("player " + PlayerToPrint.getName() + 
                           " Total Money: " + PlayerToPrint.getMoney());
    }

    @Override
    public Double GetBidForPlayer(Player BettingPlayer) {
        PrintBasicPlayerInfo(BettingPlayer);
        System.out.println("Please enter your bid - ");
        Double playerBid = UserOptions.UserDoubleInput();
               
        return playerBid;
    }

    @Override
    public void PrintBidInfo(Bid BidForPrint) {
        System.out.println("Bid Money " + BidForPrint.getTotalBid() + " cards: ");
        PrintHandInfo(BidForPrint);
    }

    @Override
    public RoundAction GetFinishRoundAction() {
        MenuMessages.RoundActionMessage();
        int IntUserChoice = 
                UserOptions.UserIntChoice(RoundAction.getSize());
        RoundAction EnumRoundAction = 
                RoundAction.values()[IntUserChoice];
         
      return EnumRoundAction;
    }
    
    private SaveOptions SaveOrSaveAs(){
        MenuMessages.SaveMessage();
        int IntUserChoice =
                UserOptions.UserIntChoice(SaveOptions.getSize());
        SaveOptions EnumSaveOption = 
                SaveOptions.values()[IntUserChoice];
        return EnumSaveOption;
    }

    @Override
    public void PrintMessage(String Message) {
        System.out.println(Message);
    }

    @Override
    public void PrintHandInfo(Hand HandForPrint) {      
        List<Card> playerCards = HandForPrint.getCards();           
        
        for (Card card : playerCards){
            System.out.println(card);
        }
        
    }

    @Override
    public void PrintParticipnatName(GameParticipant PartToPrint) {
        System.out.println("participant " + PartToPrint.getName());
    }

    private String EnterFileNameForSave() 
    {  
       FilePathForSave = UserOptions.FilePathOutput();
       return FilePathForSave; 
    }

    @Override
    public boolean DoesPlayerContinue(Player player) {
        PrintParticipnatName(player);
        System.out.println("Do you wish to continue playing? "
                            + "enter true - continue, false - exit the game ");
        return UserOptions.UserBoolChoice();
    }
    
}
