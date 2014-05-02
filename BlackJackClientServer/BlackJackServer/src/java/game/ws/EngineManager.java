/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.TooManyPlayersException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ws.blackjack.Action;
import ws.blackjack.DuplicateGameName;
import ws.blackjack.DuplicateGameName_Exception;
import ws.blackjack.Event;
import ws.blackjack.EventType;
import ws.blackjack.GameDetails;
import ws.blackjack.GameDoesNotExists;
import ws.blackjack.GameDoesNotExists_Exception;
import ws.blackjack.GameStatus;
import ws.blackjack.InvalidParameters;
import ws.blackjack.InvalidParameters_Exception;
import ws.blackjack.InvalidXML;
import ws.blackjack.InvalidXML_Exception;
import ws.blackjack.PlayerAction;
import ws.blackjack.PlayerDetails;
import ws.blackjack.PlayerStatus;
import ws.blackjack.PlayerType;

/**
 *
 * @author Yakir
 */
public class EngineManager {
    private static int uniqePlayerID = 0;
    private static int uniqeEventID = 0;
    private static HashMap<String,GameDetails> gamemanager = new HashMap<>();
    private static HashMap<Integer, PlayerDetails> playerManager = new HashMap<>();

    private static HashMap<Integer, String> IdToGame = new HashMap<>();
    private static ArrayList<Event> Events = new ArrayList<>();
    private static Double Money;
    private static GameEngineStart Engine ;
    private static Action plPlayerAction = Action.STAND;

               
    private EngineManager()
    {
    }
    
    public static ArrayList<Event> getEvents() {
        return Events;
    }
    
    public static Double getMoney() {
        return Money;
    }

    public static int getUniqeEventID() {
        return uniqeEventID++;
    }
    
    public static Action getPlPlayerAction() {
        return plPlayerAction;
    }
    
    public static HashMap<Integer, PlayerDetails> getPlayerManager() {
        return playerManager;
    }
    
    private static void ThrowInvalidParameter(String Message) throws InvalidParameters_Exception
    {
        InvalidParameters info = new InvalidParameters();
        info.setMessage(Message);
        throw new InvalidParameters_Exception(Message,info);
    }
    
    
    public static List<Event> getEvents(int playerId, int eventId) throws InvalidParameters_Exception
    {
        if(!playerManager.containsKey(playerId))
        {
            ThrowInvalidParameter("Player id does not exsists");
        }
        if(eventId > Events.size() || eventId < 0)
        {
            ThrowInvalidParameter("Error eventid is incorrect");
        }
        return Events.subList(eventId, Events.size() );
    }
    
    private static void CreateServerGame(String GameName,GameDetails gmDetail) throws DuplicateGameName_Exception, InvalidParameters_Exception
    {
        if(gamemanager.containsKey(GameName))
        {
            DuplicateGameName faultinfo = new DuplicateGameName();
            faultinfo.setMessage("Error Duplicate name");
            throw new DuplicateGameName_Exception("Error Duplicate name",faultinfo);
        }
        if(gamemanager.keySet().size() > 0)
        {
            ThrowInvalidParameter("Too many Games");
        }
        gamemanager.put(GameName, gmDetail);
    }
    
    
    public static void CreateGame(String name, int humanPlayers, int computerizedPlayers) throws DuplicateGameName_Exception, InvalidParameters_Exception
    {
        if(humanPlayers < 0 )
        {
            ThrowInvalidParameter("Need one human player");
        }
        if((humanPlayers + computerizedPlayers) > 6)
        {
            ThrowInvalidParameter("Too many players");
        }
       
        GameDetails gmDetail = new GameDetails();
        gmDetail.setHumanPlayers(humanPlayers);
        gmDetail.setComputerizedPlayers(computerizedPlayers);
        gmDetail.setJoinedHumanPlayers(0);
        gmDetail.setName(name);
        gmDetail.setStatus(GameStatus.WAITING);
        
        for (int i = 0; i < computerizedPlayers; i++) 
        {
            uniqePlayerID++;
            PlayerDetails player = new PlayerDetails();
            //player.setMoney(Money);
            player.setType(PlayerType.COMPUTER);
            player.setStatus(PlayerStatus.ACTIVE);
            player.setName("Comp" + uniqePlayerID);
            playerManager.put(uniqePlayerID, player);
            IdToGame.put(uniqePlayerID, name);
        }
        
        
        CreateServerGame(name,gmDetail);
        Engine = new GameEngineStart();
        try 
        {
            Engine.AddCompPlayers(computerizedPlayers);
        } 
        catch (TooManyPlayersException ex) 
        {
            ThrowInvalidParameter("Too many players");
        }
    }
    
    public static GameDetails GetGameDetails(String name ) throws GameDoesNotExists_Exception
    {
        if(!gamemanager.containsKey(name))
        {
            GameDoesNotExists gmInfo = new GameDoesNotExists();
            throw new GameDoesNotExists_Exception("Game does not exists",gmInfo);
        }
        
        return gamemanager.get(name);
    }
    
    public static PlayerDetails GetPlayerDetails(int PlayerId) throws InvalidParameters_Exception{
        
        if (playerManager.containsKey(PlayerId))      
            return playerManager.get(PlayerId);      
        else{
            InvalidParameters faultInfo = new InvalidParameters();
            faultInfo.setMessage("Error - player doesn`t exist");
            throw new InvalidParameters_Exception(((Integer)PlayerId).toString(), faultInfo);
        }
    }
    
    public static List<PlayerDetails> GetPlayersDetails(String GameName) throws GameDoesNotExists_Exception{
        
        List<PlayerDetails> playersDetails = new ArrayList<>();
        int ID;
            
        if (!gamemanager.containsKey(GameName)) {
            GameDoesNotExists faultInfo = new GameDoesNotExists();
            faultInfo.setMessage("Error Game Name does not exists");
            throw new GameDoesNotExists_Exception(GameName, faultInfo);  
        }
        else{
            
            for (Entry<Integer, String> players : IdToGame.entrySet())
            {
                if (players.getValue().equals(GameName)){             
                    ID = players.getKey();
                    playersDetails.add(playerManager.get(ID));
                }
            }

        }
        return playersDetails;
    }
    
    
    public static int PlayerJoinGame (String GameName, String PlayerName, float Money) 
                                        throws GameDoesNotExists_Exception, 
                                               InvalidParameters_Exception{
        
        if (!gamemanager.containsKey(GameName))
        {
            GameDoesNotExists faultInfo = new GameDoesNotExists();
            faultInfo.setMessage("Error Game Name does not exists");
            throw new GameDoesNotExists_Exception(GameName, faultInfo);
        }
        else
        {
            if (CheckIfNameExists(GameName, PlayerName))
            {
                InvalidParameters faultInfo = new InvalidParameters();
                faultInfo.setMessage("Error - name already exists");
                throw new InvalidParameters_Exception(PlayerName, faultInfo);
                
            }
            else{
                uniqePlayerID++;

                GameDetails Game = gamemanager.get(GameName);

                PlayerDetails player = new PlayerDetails();
                //player.setMoney(Money);
                player.setType(PlayerType.HUMAN);
                player.setStatus(PlayerStatus.ACTIVE);
                player.setName(PlayerName);
                try 
                {
                    Engine.AddHumanPlayers(PlayerName);
                } 
                catch (TooManyPlayersException ex) 
                {
                    InvalidParameters faultInfo = new InvalidParameters();
                    faultInfo.setMessage("Too mant players");
                    throw new InvalidParameters_Exception(PlayerName, faultInfo);
                }
                
                Game.setJoinedHumanPlayers(Game.getJoinedHumanPlayers() + 1);

                playerManager.put(uniqePlayerID, player);
                IdToGame.put(uniqePlayerID, GameName);
                
                if(Game.getJoinedHumanPlayers() == Game.getHumanPlayers())
                {
                    Event StartGame = new Event();
                    StartGame.setId(getUniqeEventID());
                    StartGame.setType(EventType.GAME_START);
                    Events.add(StartGame);
                    Engine.setDaemon(true);
                    Engine.start();
                }
                
            }
        }
        return uniqePlayerID;
    }
    
    private static boolean CheckIfNameExists(String GameName, String PlayerName) {
       
        boolean NameExists = false;
        
        for (Entry<Integer, PlayerDetails> entry : playerManager.entrySet()) {
            PlayerDetails player = entry.getValue();
            
            if (player.getName().equals(PlayerName))
                NameExists = true;        
        }
        
        return NameExists;
    }
    
    public static List<String> GetActiveGames(){
        
        List<String> ActiveGames = new ArrayList<>();
        
        for (Entry<String, GameDetails> game : gamemanager.entrySet())
        {
            if (game.getValue().getStatus().equals(GameStatus.ACTIVE))
            {
                ActiveGames.add(game.getKey());
            }
        }
        return ActiveGames;
    }
    
        public static List<String> GetWaitingGames(){
        
        List<String> WaitingGames = new ArrayList<>();
        
        for (Entry<String, GameDetails> game : gamemanager.entrySet())
        {
            if (game.getValue().getStatus().equals(GameStatus.WAITING))
            {
                WaitingGames.add(game.getKey());
            }
        }
        return WaitingGames;
    }
        
    public static void PlayerResign(int PlayerId) throws InvalidParameters_Exception
    {
        if (!playerManager.containsKey(PlayerId)){
            InvalidParameters faultInfo = new InvalidParameters();
            faultInfo.setMessage("Error - player doesn`t exist");
            throw new InvalidParameters_Exception(((Integer)PlayerId).toString(), faultInfo);    
        }                 
        else{
            PlayerDetails player = playerManager.get(PlayerId);
            player.setStatus(PlayerStatus.RETIRED);
            IdToGame.remove(PlayerId);
            
            Event playerResign  = new Event();
            playerResign.setId(getUniqeEventID());          
            playerResign.setPlayerName(player.getName());      
            playerResign.setType(EventType.PLAYER_RESIGNED);
            Events.add(playerResign);
            //TODO: remove player from engine
        }
            
        
    }
    
    public static String CreateGameFromXML(String XMLData) throws DuplicateGameName_Exception, InvalidParameters_Exception, InvalidXML_Exception
    {
        try 
        {
            Engine = new GameEngineStart(XMLData);
            GameDetails gmDetail = new GameDetails();
            gmDetail.setHumanPlayers(Engine.GetHumanPlayers());
            gmDetail.setComputerizedPlayers(Engine.GetCompPlayers());
            gmDetail.setJoinedHumanPlayers(0);
            gmDetail.setName(Engine.GetGameName());
            gmDetail.setStatus(GameStatus.WAITING);
        
            CreateServerGame(Engine.GetGameName(),gmDetail);
        }
        catch (DuplicateCardException | SAXException | TooManyPlayersException | JAXBException ex) 
        {
            InvalidXML exXml = new InvalidXML();
            throw new InvalidXML_Exception("XML File is not valid", exXml);
        }
        return Engine.GetGameName();
    }
    
    public static void Playeraction(int playerId, int eventId, ws.blackjack.Action action, float money, int bet) throws InvalidParameters_Exception
    {
        if(!playerManager.containsKey(playerId))
        {
            InvalidParameters faultInfo = new InvalidParameters();
            faultInfo.setMessage("Error - player doesn`t exist");
            throw new InvalidParameters_Exception(((Integer)playerId).toString(), faultInfo);    
        }
        if(Events.size() != eventId)
        {
           InvalidParameters faultInfo = new InvalidParameters();
           faultInfo.setMessage("Error - event id not last");
           throw new InvalidParameters_Exception(((Integer)playerId).toString(), faultInfo);     
        }
        
        Event playerAction = new Event();
        playerAction.setType(EventType.USER_ACTION);
        playerAction.setId(getUniqeEventID());
        playerAction.setPlayerName(playerManager.get(playerId).getName());
        playerAction.setPlayerAction(action);
        
        /*
        if (action.equals(Action.PLACE_BET)){
            Money = (double) money;
            Money.notify();
        }*/
        
        plPlayerAction = action;
        plPlayerAction.notifyAll();
            
    }
    
    
}
