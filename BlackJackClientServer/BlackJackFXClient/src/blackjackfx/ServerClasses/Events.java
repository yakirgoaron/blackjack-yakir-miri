/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.ServerClasses;

import blackjackfx.Controllers.GameScreenController;
import blackjackfx.ScreenManager;
import game.client.ws.Action;
import game.client.ws.BlackJackWebService;
import game.client.ws.BlackJackWebService_Service;
import game.client.ws.DuplicateGameName_Exception;
import game.client.ws.Event;
import game.client.ws.GameDetails;
import game.client.ws.GameDoesNotExists_Exception;
import game.client.ws.InvalidParameters_Exception;
import game.client.ws.InvalidXML_Exception;
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
    private Boolean GameStarted;
    private String FilePath;
    private int PlayerID;
    private String PlayerName;
    private String GameName;
    private int EventID;
    
    
    public Events(String serverAddress ,String  serverPort) throws MalformedURLException
    {
        URL url = new URL("http://" + serverAddress + ":" + serverPort + "/bjwebapi/BlackJackWebService");
        BlackJackWebService_Service WSForConnect = new BlackJackWebService_Service(url);
        GameWS = WSForConnect.getBlackJackWebServicePort();
        GameEnded = new SimpleBooleanProperty();
        GameName = new String();
        GameStarted = false;
        EventID = 0;
        
    }
    
    public void SetController(GameScreenController Controller){
        scControoler = Controller;
    }
    
    public void setGameName(String GameName) {
        this.GameName = GameName;
    }
    public void setPlayerID(int SetPlayerID) {
        this.PlayerID = SetPlayerID;
    }
    
    public SimpleBooleanProperty getGameEnded() {
        return GameEnded;
    }
    
       
    public void CreateGame(String GameName, int HumanPlayers, int ComputerizedPlayers ) throws DuplicateGameName_Exception, InvalidParameters_Exception
    {
        GameWS.createGame(GameName, HumanPlayers, ComputerizedPlayers);
        this.GameName = GameName;
    }
    public void CreateGameFromXML(String xmlData ) throws DuplicateGameName_Exception, InvalidParameters_Exception, InvalidXML_Exception
    {
        this.GameName = GameWS.createGameFromXML(xmlData);        
    }
    
    public void DoesPlayerContinue(final PlayerInfo player) 
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
         
        if (!scControoler.getDoesPlayerContinue().get())
            try {
            GameWS.resign(PlayerID);
        } catch (InvalidParameters_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public List<String> GetWaitingGames()
    {
        return GameWS.getWaitingGames();
    }
    
    public GameInfo GetGameDetails(String Name) throws GameDoesNotExists_Exception
    {
        return new GameInfo(GameWS.getGameDetails(Name));
    }
    
    private PlayerInfo GetPlayerDetailsByName(String Name) 
    {
        try
        {
            List<PlayerDetails> Players = GameWS.getPlayersDetails(GameName);

            for (PlayerDetails playerDetails : Players) 
            {
                 if(playerDetails.getName().equals(Name))
                     return new PlayerInfo(playerDetails);
            }
        } 
        catch (GameDoesNotExists_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private void DisplayPlayerCards(Event event)
    {
        PrintBidInfo(GetPlayerDetailsByName(event.getPlayerName()));        
    }
    
    private void DisplayPlayerEffect(final Event event)
    {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DiplayEffect(event.getPlayerName());
                                }});  
    }
    
    
    
    private void PromptOptionsToUser(Event event)
    {
        try
        {
            PlayerDetails MyInfo = GameWS.getPlayerDetails(PlayerID);
            if(MyInfo.getName().equals(event.getPlayerName()))
            {
                if(MyInfo.getFirstBetWage() <= 0.0)
                {
                    try {
                            GetBidForPlayer(new PlayerInfo(GameWS.getPlayerDetails(PlayerID)));
                        } catch (GameDoesNotExists_Exception ex) {
                            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidParameters_Exception ex) {
                            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                else
                {
                    GetWantedAction();
                }
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
                    PrintAllJoinedPlayers();
                    GameStarted = true;
                    GameEnded.set(false);
                    break;
                }
                case GAME_WINNER:
                    PrintGameWinner(GetPlayerDetailsByName(event.getPlayerName()));
                    break;
                case NEW_ROUND:
                {                   
                    if (event.getPlayerName().equals(PlayerName)){
                        PlayerInfo player = GetPlayerDetailsByName(PlayerName);
                        DisplayPlayer(player);
                        DoesPlayerContinue(player);
                    }
                    ClearTable();
                    
                        
                    break;
                }
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
                    PrintPlayerMessage(GetPlayerDetailsByName(event.getPlayerName()),event.getPlayerAction().value());
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
                if (!GameName.equals("")){
                    List<Event> EventsHappened = GameWS.getEvents(PlayerID, EventID);
                    DealWithEvents(EventsHappened);
                }
                Thread.sleep(1000);
                
            
            } catch (InterruptedException ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidParameters_Exception ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
   
    public String GetFilePathForSave() 
    {
        /*
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
         */
        
        return null;
    }
    
    public List<PlayerInfo> GetPlayersInGame()
    {
        List<PlayerInfo> players = new ArrayList<>();
        try 
        {
            for (PlayerDetails playerInfo : GameWS.getPlayersDetails(GameName))
            {
                players.add(new PlayerInfo(playerInfo));
            }
        } 
        catch (GameDoesNotExists_Exception ex) 
        {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
        return players; 
    }
    
    public void GetWantedAction() 
    {
        try {
                synchronized(scControoler.getPlayerActionType())
                {
                    scControoler.ShowActions();
                    
                    scControoler.getPlayerActionType().wait();
                }
            
            Action actionchoosed = Action.valueOf(scControoler.getPlayerActionType().get().name());
            // TODO DEAL WITH THE BET
            GameWS.playerAction(PlayerID, EventID, actionchoosed, 0,1);
        } catch (InvalidParameters_Exception ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
                
            }
    }

        
    public void PrintBasicPlayerInfo(final PlayerInfo PlayerToPrint) {
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
        PlayerID = GameWS.joinGame(GameName, Name, 100);
        PlayerName = Name;
    }
   
    public void GetBidForPlayer(final PlayerInfo BettingPlayer) throws InvalidParameters_Exception {
        
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
        GameWS.playerAction(PlayerID, EventID, Action.PLACE_BET,BidValue.floatValue(),1);
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                      scControoler.GetHideBidWindow().set(true);
                                }}); 
        
        // TODO UPDATE USER BID AT THE START
    }

    
   /* TODO: CHECK IF NEED WE PRINT THE PLAYER FROM THE EVENT*/ 
    public void PrintBidInfo(final PlayerInfo PlayerBid) {
       Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayBid(PlayerBid.getName() + "1", PlayerBid);
                                }}); 
    }

   /*
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

    
    

    
    public void RemovePlayer(final PlayerInfo player) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.RemovePlayer(player);
                                }}); 
    }

    
    public void PrintPlayerMessage(final PlayerInfo ParPlayer, final String Message) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.PrintPlayerMessage(ParPlayer, Message);
                                }});
    }

    private void PrintAllJoinedPlayers() {    
               Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     List<PlayerInfo> players;
                                     players = GetPlayersInGame();
                                    scControoler.AddPlayerToGame(players);
                                }});
            
    }

    private void PrintGameWinner(final PlayerInfo PlayerWin) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                {                                     
                                    scControoler.PrintPlayerMessage(PlayerWin, "WINNER!!!!!!");
                                }});
    }

    private void DisplayPlayer(final PlayerInfo player) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayPlayer(player);
                                }});
    }
   
}
