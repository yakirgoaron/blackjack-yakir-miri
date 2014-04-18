/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import GameEnums.SaveOptions;
import blackjackfx.Controllers.GameScreenController;
import game.client.ws.Action;
import game.client.ws.BlackJackWebService;
import game.client.ws.BlackJackWebService_Service;
import game.client.ws.Event;
import game.client.ws.GameDoesNotExists_Exception;
import game.client.ws.InvalidParameters_Exception;
import game.client.ws.PlayerAction;
import game.client.ws.PlayerDetails;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

/**
 *
 * @author yakir
 */
public class Events extends Thread
{
    private BlackJackWebService GameWS;
    private GameScreenController scControoler;
    private SimpleBooleanProperty GameEnded;
    private String FilePath;
    private int PlayerID;
    private String GameName;

   
    private int EventID;
    
    
    public Events(String serverAddress ,String  serverPort,String GameName) throws MalformedURLException
    {
        URL url = new URL("http://" + serverAddress + ":" + serverPort + "/webapi/BlackJackWebService");
        BlackJackWebService_Service WSForConnect = new BlackJackWebService_Service(url);
        GameWS = WSForConnect.getBlackJackWebServicePort();
        EventID = 0;
        this.GameName = GameName;
    }
    
    public void setPlayerID(int SetPlayerID) {
        this.PlayerID = SetPlayerID;
    }
    
    public SimpleBooleanProperty getGameEnded() {
        return GameEnded;
    }
    
    public void DoesPlayerContinue(final PlayerDetails player) 
    {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.ShowPlayerContGame(player.getName());
                                }});      
         try 
        {
            synchronized(scControoler.getDoesPlayerContinue())
            {               

                scControoler.getDoesPlayerContinue().wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO CReaTe eveNT AND SEND TO THE SERVER 
        scControoler.getDoesPlayerContinue().get();
       
    }
    
    private PlayerDetails GetPlayerDetailsByName(String Name) 
    {
        try
        {
            List<PlayerDetails> Players = GameWS.getPlayersDetails(GameName);

            for (PlayerDetails playerDetails : Players) 
            {
                 if(playerDetails.getName().equals(Name))
                     return playerDetails;
            }
        } 
        catch (GameDoesNotExists_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private void DisplayPlayerCards(Event event)
    {
        PrintBasicPlayerInfo(GetPlayerDetailsByName(event.getPlayerName()));        
    }
    
    private void DisplayPlayerEffect(final Event event)
    {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DiplayEffect(GetPlayerDetailsByName(event.getPlayerName()));
                                }});  
    }
    
    
    
    private void PromptOptionsToUser(Event event)
    {
        try
        {
            PlayerDetails MyInfo = GameWS.getPlayerDetails(GameName, PlayerID);
            if(MyInfo.getName().equals(event.getPlayerName()))
            {
                GetWantedAction();
            }
        }
        catch (GameDoesNotExists_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidParameters_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }       
            
    
    private void DealWithEvents(List<Event> EventsHappened)
    {
        EventID += EventsHappened.size();
        for (Event event : EventsHappened) 
        {
            
            switch(event.getType())
            {
                case CARDS_DEALT:
                {
                    DisplayPlayerCards(event);
                    break;
                }
                case GAME_OVER:
                {
                    GameEnded.set(true);
                    break;
                }
                case GAME_START:
                {
                    GameEnded.set(false);
                    break;
                }
                case GAME_WINNER:
                    break;
                case NEW_ROUND:
                    break;
                case PLAYER_RESIGNED:
                {
                    RemovePlayer(GetPlayerDetailsByName(event.getPlayerName()));
                    break;
                }
                case PLAYER_TURN:
                {
                   DisplayPlayerEffect(event);
                   break;
                }
                case PROMPT_PLAYER_TO_TAKE_ACTION:
                {
                    PromptOptionsToUser(event);
                    break;
                }
                case USER_ACTION:
                {
                    // TODO change the action that given
                    PrintPlayerMessage(GetPlayerDetailsByName(event.getPlayerName()),"NULL ACTION");
                    break;
                }
            }
        }
    }
    
    @Override
    public void run() 
    {   
        boolean FlagStart = true;
        while(FlagStart)
        {
            try 
            {
                List<Event> EventsHappened = GameWS.getEvents(PlayerID, EventID);
                DealWithEvents(EventsHappened);
                Thread.sleep(30000);
            
            } catch (InterruptedException ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidParameters_Exception ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
   
    public String GetFilePathForSave() 
    {
       SaveOptions UserChoice;
        
        if (FilePath == null){
            scControoler.GetFilePathToSave();
            FilePath = scControoler.GetPath().get();
        }
        else {
            UserChoice = SaveOrSaveAs();
            
            if (UserChoice.equals(SaveOptions.SAVE_AS)){
                scControoler.GetFilePathToSave();
                FilePath = scControoler.GetPath().get();
            }           
        }       
        return FilePath;
    }
    
    private SaveOptions SaveOrSaveAs() {       
        
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.ShowSaveOptions();
                                }});    
        
        try 
        {
            synchronized(scControoler.getPlayerSaveType())
            {               
                scControoler.getPlayerSaveType().wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);

        }
        return scControoler.getPlayerSaveType().get();
    }

    public List<PlayerDetails> getGamePlayers()
    {
        try {
            return GameWS.getPlayersDetails(GameName);
        } catch (GameDoesNotExists_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void GetWantedAction() 
    {
        try {
                synchronized(scControoler.getPlayerActionType())
                {
                    scControoler.ShowActions();
                    
                    scControoler.getPlayerActionType().wait();
                }
            
            Action actionchoosed = scControoler.getPlayerActionType().get();
            // TODO DEAL WITH THE BET
            GameWS.playerAction(PlayerID, EventID, actionchoosed, 0);
        } catch (InvalidParameters_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
                
            }
    }

        
    public void PrintBasicPlayerInfo(final PlayerDetails PlayerToPrint) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayPlayer(PlayerToPrint);
                                }});
        
    }

    public void JoinGame(String Name) throws GameDoesNotExists_Exception, InvalidParameters_Exception
    {
        // MONEY SHOULD BE GET FROM USER ???
        GameWS.joinGame(GameName, GameName, 100);
    }
   /* public void GetFinishRoundAction() {
       
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                      scControoler.GetHideBidWindow().set(true);
                                }});
        
        try 
        {
            synchronized(scControoler.getRoundChoice())
            {
                ClearTable();
                scControoler.ShowRoundActions();
                
                scControoler.getRoundChoice().wait();
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            
        }
         scControoler.getRoundChoice().get();
    }*/

   
    public void GetBidForPlayer(final PlayerDetails BettingPlayer) {
        
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                      ScreenManager.GetInstance().getBidScCr().SetPlayer(BettingPlayer);
                                      scControoler.GetHideBidWindow().set(false);
                                }});       
        
        try 
        {
            synchronized(ScreenManager.GetInstance().getBidScCr().GetNumberBid())
            {
                ScreenManager.GetInstance().getBidScCr().GetNumberBid().wait();
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            
        } 
        
        Double BidValue = ScreenManager.GetInstance().getBidScCr().GetNumberBid().getValue();
        
        // TODO UPDATE USER BID AT THE START
    }

    
   /* TODO: CHECK IF NEED WE PRINT THE PLAYER FROM THE EVENT 
    public void PrintBidInfo(final Bid BidForPrint, final Player PlayerBid) {
       Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayBid(BidForPrint, PlayerBid);
                                }}); 
    }

   
    public void PrintHandInfo(final Hand HandForPrint,final GameParticipant ParPlayer) {
        try {
            Platform.runLater(new Runnable(){
                @Override
                public void run()
                {
                    scControoler.DisplayHand(HandForPrint, ParPlayer);
                }});
            if(ParPlayer instanceof CompPlayer || ParPlayer instanceof Dealer)
                Thread.sleep(2100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

   /*public void PrintAllPlayers(final ArrayList<Player> GamePlayers) {
       
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.GetHideBidWindow().set(true);
                                     scControoler.ShowPlayers(GamePlayers);
                                }});
    }*/
    public void PrintMessage(final String Message) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayMessage(Message);
                                }}); 
    }

    private void ClearTable() {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.ClearTable();
                                }});        
    }

    
    

    
    public void RemovePlayer(final PlayerDetails player) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.RemovePlayer(player);
                                }}); 
    }

    
    public void PrintPlayerMessage(final PlayerDetails ParPlayer, final String Message) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.PrintPlayerMessage(ParPlayer, Message);
                                }});
    }
   
}
