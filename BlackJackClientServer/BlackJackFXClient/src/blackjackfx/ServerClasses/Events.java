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
import game.client.ws.GameDoesNotExists_Exception;
import game.client.ws.InvalidParameters_Exception;
import game.client.ws.InvalidXML_Exception;
import game.client.ws.PlayerDetails;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author yakir
 */
public class Events extends Thread
{
    private BlackJackWebService GameWS;
    private GameScreenController scControoler;
    private int PlayerID;
    private String PlayerName;
    private String GameName;
    private int EventID;
    private int HandToTake = 0;
    private boolean IsSplitChosen = false;
    private double InvalidBid;
    private boolean IsResigned = false;
    
    public Events(String serverAddress ,String  serverPort) throws MalformedURLException
    {
        URL url = new URL("http://" + serverAddress + ":" + serverPort + "/bjwebapi/BlackJackWebService");
        BlackJackWebService_Service WSForConnect = new BlackJackWebService_Service(url);
        GameWS = WSForConnect.getBlackJackWebServicePort();
        GameName = new String();
        setDaemon(true);
        EventID = 0;
        InvalidBid = ScreenManager.GetInstance().getBidScCr().getInvalidBid();
        
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
       
    public void CreateGame(String GameName, int HumanPlayers, int ComputerizedPlayers ) throws DuplicateGameName_Exception, InvalidParameters_Exception
    {
        GameWS.createGame(GameName, HumanPlayers, ComputerizedPlayers);
        this.GameName = GameName;
    }
    public void CreateGameFromXML(String xmlData ) throws DuplicateGameName_Exception, InvalidParameters_Exception, InvalidXML_Exception
    {
        this.GameName = GameWS.createGameFromXML(xmlData);        
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
            
        }
        return null;
    }
    private void DisplayPlayerCards(Event event)
    {
        PlayerInfo player = GetPlayerDetailsByName(event.getPlayerName());
        if(player != null)
        {
            if(event.getPlayerName().equals(PlayerName))
            {
                player.getBets().get(HandToTake).setBetCards(player.ConvertPlayerCards(event.getCards()));
                player.getBets().get(HandToTake).setBetWage(event.getMoney());
            }
            PrintBidInfo(player);        
        }
    }
    
    private void DisplayPlayerAndEffect(final Event event)
    {
        PrintBasicPlayerInfo(GetPlayerDetailsByName(event.getPlayerName())); 
    }
    
    
    
    private void PromptOptionsToUser(Event event)
    {
        try
        {
            PlayerDetails MyInfo = GameWS.getPlayerDetails(PlayerID);
            if(MyInfo.getName().equals(event.getPlayerName()) && !MyInfo.getStatus().value().equals(PlayerStatus.RETIRED.value()) )
            {
                if(MyInfo.getFirstBetWage() <= 0.0)
                {
                    try {
                            GetBidForPlayer(new PlayerInfo(GameWS.getPlayerDetails(PlayerID)), event.getTimeout());
                        } catch (GameDoesNotExists_Exception ex) {
                            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidParameters_Exception ex) {
                            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                else
                {
                    GetWantedAction(event.getTimeout());
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
                    GameOver(); 
                   
                    break;
                }
                case GAME_START:
                {
                    PrintAllJoinedPlayers();
                    break;
                }
                case GAME_WINNER:
                {
                    PlayerInfo player = GetPlayerDetailsByName(event.getPlayerName());
                    if(player != null)
                        PrintGameWinner(player);
                    break;
                }
                case NEW_ROUND:
                {                   
                    if (event.getPlayerName().equals(PlayerName)){
                        
                        DisplayPlayers();
                        PrintNewRound();
                        HandToTake =0;
                        IsSplitChosen = false;
                        ClearTable();
                    }                       
                    break;
                }
                case PLAYER_RESIGNED:
                {
                    String Name = event.getPlayerName();
                    RemovePlayer(Name);
                    
                    if (Name.equals(PlayerName)){
                        IsResigned = true;
                        DisableResign();                       
                        GameOver();
                    }
                    break;
                }
                case PLAYER_TURN:
                {
                   DisplayPlayerAndEffect(event);
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
                if (!GameName.equals("") && !IsResigned){
                    List<Event> EventsHappened = GameWS.getEvents(PlayerID, EventID);
                    DealWithEvents(EventsHappened);
                }
                Thread.sleep(1000);
                
            
            } catch (InterruptedException ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidParameters_Exception ex) {
                GameOver();
            }
        }
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
    
    public void GetWantedAction(int Timeout) 
    {
        try {
                synchronized(scControoler.getPlayerActionType())
                {
                    scControoler.ShowActions();                    
                    scControoler.getPlayerActionType().wait(Timeout);
                }
            if (!scControoler.getIsActionChosen())
                PlayerResign();
            scControoler.setIsActionChosen(false);
            Action actionchoosed = Action.valueOf(scControoler.getPlayerActionType().get().name());
            List<Event> eventdone = GameWS.getEvents(PlayerID, EventID);
            DealWithEvents(eventdone);
            GameWS.playerAction(PlayerID, EventID, actionchoosed, 0,HandToTake+1);
            if(actionchoosed.equals(Action.SPLIT))
                IsSplitChosen = true;
            if(!actionchoosed.equals(Action.SPLIT) && !actionchoosed.equals(Action.HIT) && HandToTake < 1 && IsSplitChosen)
                HandToTake++;
            Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                      scControoler.DisplayMessage("");
                                }}); 
        } catch (final InvalidParameters_Exception ex) {
            Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                      scControoler.DisplayMessage(ex.getMessage());
                                }});
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
        PlayerID = GameWS.joinGame(GameName, Name, 100);
        PlayerName = Name;
    }
   
    public void GetBidForPlayer(final PlayerInfo BettingPlayer, int Timeout) throws InvalidParameters_Exception {
                   
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
                ScreenManager.GetInstance().getBidScCr().GetNumberBid().wait(Timeout);
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            
        } 
        
        Double BidValue = ScreenManager.GetInstance().getBidScCr().GetNumberBid().getValue();;
        
        if (BidValue != InvalidBid){
            
            GameWS.playerAction(PlayerID, EventID, Action.PLACE_BET,BidValue.floatValue(),1);
            ScreenManager.GetInstance().getBidScCr().SetNumberBid(InvalidBid);
        }
            Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() 
                                    { 
                                          scControoler.GetHideBidWindow().set(true);
                                    }});     
                                   
    }
 
    public void PrintBidInfo(final PlayerInfo PlayerBid) {
       Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayBid(PlayerBid.getName() + (HandToTake + 1), PlayerBid);
                                }}); 
    }

   
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

    
    public void RemovePlayer(final String playerName) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.RemovePlayer(playerName);
                                }}); 
    }

    
    public void PrintPlayerMessage(final PlayerInfo ParPlayer, final String Message) {
        
        if (ParPlayer != null){
            Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() 
                                    { 
                                         scControoler.PrintPlayerMessage(ParPlayer, Message);
                                    }});
        }
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

    private void DisplayPlayers() {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    List<PlayerInfo> players = GetPlayersInGame();
                                    
                                    for (PlayerInfo player : players) { 
                                        
                                        if(!player.getName().equals("Dealer"))                                       
                                            scControoler.DisplayPlayer(player);
                                    }                                                                        
                                }});
    }

    private void PrintNewRound() {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                {                                       
                                    scControoler.DisplayMessage("New Round");
                                }});    
    }
    
    public void PlayerResign() {
        try {
            GameWS.resign(PlayerID);
            GameOver();
            
        } catch (InvalidParameters_Exception ex) {
            GameOver();
        }
    }

    private void DisableResign() {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                {                                       
                                    scControoler.DisableResign();
                                }});    
    }

    public int getHandToTake() {
        return HandToTake;
    }

    private void GameOver() {
        Platform.runLater(new Runnable(){
            @Override
            public void run() 
            { 
                   scControoler.getGameEnded().set(true);
            }});  
    }   
   
}
