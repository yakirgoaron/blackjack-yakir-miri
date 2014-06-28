/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.TooManyPlayersException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import ws.blackjack.PlayerDetails;
import ws.blackjack.PlayerStatus;
import ws.blackjack.PlayerType;

/**
 *
 * @author Yakir
 */
public class EngineManager {
    private static int uniqePlayerID = 0;
    //private static int uniqeEventID = 0;
    private static int CompIdGen=1;
    private static HashMap<String,GameManager> gamemanager = new HashMap<>();
    private static HashMap<Integer, PlayerDetails> playerManager = new HashMap<>();

    private static HashMap<Integer, String> IdToGame = new HashMap<>();
    
    private static Double Money = null;
    private static Action plPlayerAction = null;
    private static Boolean StopWait = false;

              
    private EngineManager()
    {
    }
    public static void ClearData(String GameName)
    {
        Iterator<Map.Entry<Integer,String>> iter = IdToGame.entrySet().iterator();
        
        while (iter.hasNext()) {
            Map.Entry<Integer,String> entry = iter.next();
            if(entry.getValue().equals(GameName)){
                iter.remove();
                playerManager.remove(entry.getKey());
            }
        }
    
        gamemanager.remove(GameName);
    }
    
    public static Boolean isStopWait() {
        return StopWait;
    }
    
    public static Double getMoney() {
        return Money;
    }

    public static void setMoney(Double Money) {
        EngineManager.Money = Money;
    }

    
    /*public static int getUniqeEventID() {
        return uniqeEventID++;
    }*/
    
    public static Action getPlPlayerAction() {
        return plPlayerAction;
    }

    public static void setPlPlayerAction(Action plPlayerAction) {
        EngineManager.plPlayerAction = plPlayerAction;
    }
    
    public static HashMap<Integer, PlayerDetails> getPlayerManager() {
        return playerManager;
    }

    public static int getUniqePlayerID() {
        return uniqePlayerID++;
    }

    public static HashMap<Integer, String> getIdToGame() {
        return IdToGame;
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
        GameManager gm = gamemanager.get(IdToGame.get(playerId));
        if(eventId > gm.GetEvents().size() || eventId < 0)
        {
            ThrowInvalidParameter("Error eventid is incorrect");
        }
        return gm.GetEvents().subList(eventId, gm.GetEvents().size() );
    }
    
    private static void CreateServerGame(String GameName,GameManager gmDetail) throws DuplicateGameName_Exception, InvalidParameters_Exception
    {
        if(gamemanager.containsKey(GameName))
        {
            DuplicateGameName faultinfo = new DuplicateGameName();
            faultinfo.setMessage("Error Duplicate name");
            throw new DuplicateGameName_Exception("Error Duplicate name",faultinfo);
        }
        
        /*GameManager newGame = new GameManager();
        newGame.setGmDetails(gmDetail);*/
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
            player.setType(PlayerType.COMPUTER);
            player.setStatus(PlayerStatus.JOINED);
            player.setName("Comp" + CompIdGen);
            playerManager.put(uniqePlayerID, player);
            IdToGame.put(uniqePlayerID, name);
            CompIdGen++;
        }
        
        uniqePlayerID++;
        PlayerDetails Dealer = new PlayerDetails();
        Dealer.setStatus(PlayerStatus.ACTIVE);
        Dealer.setName("Dealer");
        Dealer.setType(PlayerType.COMPUTER);
        Dealer.setMoney(0);
        playerManager.put(uniqePlayerID, Dealer);
        IdToGame.put(uniqePlayerID, name);
        GameManager temp = new GameManager();
        temp.setGmDetails(gmDetail);
        CreateServerGame(name,temp);
        
        try 
        {
            gamemanager.get(name).getEngine().AddCompPlayers(computerizedPlayers);
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
        
        return gamemanager.get(name).getGmDetails();
    }
    
    public static PlayerDetails GetPlayerDetails(int PlayerId) throws InvalidParameters_Exception{
        
        if (playerManager.containsKey(PlayerId))      
            return playerManager.get(PlayerId);      
        else
        {
            ThrowInvalidParameter("Error - player doesn`t exist");
            return null;
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
                    System.out.println("GAME IS "+ GameName +" PLAYER IS " + playerManager.get(ID).getName());
                }
            }

        }
        return playersDetails;
    }
    
    
    public static int PlayerJoinGame (String GameName, String PlayerName, float Money) 
                                        throws GameDoesNotExists_Exception, 
                                               InvalidParameters_Exception{
        PlayerDetails player;
        if (!gamemanager.containsKey(GameName))
        {
            GameDoesNotExists faultInfo = new GameDoesNotExists();
            faultInfo.setMessage("Error Game Name does not exists");
            throw new GameDoesNotExists_Exception(GameName, faultInfo);
        }
        else
        {
            GameManager Game = gamemanager.get(GameName);
            player = CheckIfNameExists(PlayerName);
            if (player != null && !Game.getGmDetails().isLoadedFromXML())
            {
                ThrowInvalidParameter("Error - name already exists");
                
            }
            else if(Game.getGmDetails().isLoadedFromXML() && player != null && player.getStatus().equals(PlayerStatus.ACTIVE))
            {
                 Game.getGmDetails().setJoinedHumanPlayers(Game.getGmDetails().getJoinedHumanPlayers() + 1);
                 player.setStatus(PlayerStatus.JOINED);
            }
            else if(Game.getGmDetails().isLoadedFromXML() && player != null && player.getStatus().equals(PlayerStatus.JOINED))
            {
                ThrowInvalidParameter("Error - the player is taken");
            }
            else if(Game.getGmDetails().getJoinedHumanPlayers() == Game.getGmDetails().getHumanPlayers())
            {
                ThrowInvalidParameter("Too many players");
            }
            else
            {
                uniqePlayerID++;

                player = new PlayerDetails();
                player.setMoney(Money);
                player.setType(PlayerType.HUMAN);
                player.setStatus(PlayerStatus.ACTIVE);
                player.setName(PlayerName);
                try 
                {
                    Game.getEngine().AddHumanPlayers(PlayerName);
                } 
                catch (TooManyPlayersException ex) 
                {
                    ThrowInvalidParameter("Too many players");
                }
                
                Game.getGmDetails().setJoinedHumanPlayers(Game.getGmDetails().getJoinedHumanPlayers() + 1);

                playerManager.put(uniqePlayerID, player);
                IdToGame.put(uniqePlayerID, GameName); 
            }
            if(Game.getGmDetails().getJoinedHumanPlayers() == Game.getGmDetails().getHumanPlayers())
            {
                Event StartGame = new Event();
                StartGame.setId(Game.GetEvents().size()+1);
                Game.getGmDetails().setStatus(GameStatus.ACTIVE);
                StartGame.setType(EventType.GAME_START);
                Game.GetEvents().add(StartGame);
                Game.getEngine().setDaemon(true);
                Game.getEngine().start();
            }
        }
        gamemanager.get(GameName).getEngine().SyncAllPlayers();
        return PlayerIDFromName(player.getName());
    }
    
    
    private static int PlayerIDFromName(String Name)
    {
        for (Entry<Integer, PlayerDetails> entry : playerManager.entrySet()) {
            PlayerDetails player = entry.getValue();
            
            if (player.getName().equals(Name))
                return entry.getKey();        
        }
        
        return -1;
    }
    private static PlayerDetails CheckIfNameExists(String PlayerName) {
       
        
        for (Entry<Integer, PlayerDetails> entry : playerManager.entrySet()) {
            PlayerDetails player = entry.getValue();
            
            if (player.getName().equals(PlayerName))
                return player;        
        }
        
        return null;
    }
    
    public static List<String> GetActiveGames(){
        
        List<String> ActiveGames = new ArrayList<>();
        
        for (Entry<String, GameManager> game : gamemanager.entrySet())
        {
            if (game.getValue().getGmDetails().getStatus().equals(GameStatus.ACTIVE))
            {
                ActiveGames.add(game.getKey());
            }
        }
        return ActiveGames;
    }
    
    public static List<String> GetWaitingGames(){
        
        List<String> WaitingGames = new ArrayList<>();
        
        for (Entry<String, GameManager> game : gamemanager.entrySet())
        {
            if (game.getValue().getGmDetails().getStatus().equals(GameStatus.WAITING))
            {
                WaitingGames.add(game.getKey());
            }
        }
        return WaitingGames;
    }
        
    public static void PlayerResign(int PlayerId) throws InvalidParameters_Exception
    {
        if (!playerManager.containsKey(PlayerId)){
            ThrowInvalidParameter("Error - player doesn`t exist");  
        }                 
        else{
            PlayerDetails player = playerManager.get(PlayerId);
            player.setStatus(PlayerStatus.RETIRED);
            String Game = IdToGame.get(PlayerId);
            IdToGame.remove(PlayerId);
            
            Event playerResign  = new Event();
            playerResign.setId(gamemanager.get(Game).GetEvents().size()+1);          
            playerResign.setPlayerName(player.getName());      
            playerResign.setType(EventType.PLAYER_RESIGNED);
            gamemanager.get(Game).GetEvents().add(playerResign);
            
            synchronized(StopWait)
            {
                if(gamemanager.get(Game).getEngine().getCurrPlayer().equals(playerManager.get(PlayerId)))
                    StopWait.notifyAll();
            }
        }
            
        
        
    }
    
    public static String CreateGameFromXML(String XMLData) throws DuplicateGameName_Exception, InvalidParameters_Exception, InvalidXML_Exception
    {
        String GameName = null;
        try 
        {
            GameManager Game = new GameManager(XMLData);
            GameDetails gmDetail = new GameDetails();
            gmDetail.setHumanPlayers(Game.getEngine().GetHumanPlayers());
            gmDetail.setComputerizedPlayers(Game.getEngine().GetCompPlayers());
            gmDetail.setJoinedHumanPlayers(0);
            gmDetail.setName(Game.getEngine().GetGameName());
            gmDetail.setStatus(GameStatus.WAITING);
            gmDetail.setLoadedFromXML(true);
            Game.setGmDetails(gmDetail);
            CreateServerGame(Game.getEngine().GetGameName(),Game);
            Game.getEngine().CreatePlayerDetailsEngMng(Game.getEngine().GetGameName());
            GameName = Game.getEngine().GetGameName();
        }
        catch (DuplicateCardException | SAXException | TooManyPlayersException | JAXBException ex) 
        {
            InvalidXML exXml = new InvalidXML();
            throw new InvalidXML_Exception("XML File is not valid", exXml);
        }
        if(GameName != null)
        {
            uniqePlayerID++;
            PlayerDetails Dealer = new PlayerDetails();
            Dealer.setStatus(PlayerStatus.ACTIVE);
            Dealer.setName("Dealer");
            Dealer.setType(PlayerType.COMPUTER);
            Dealer.setMoney(0);
            playerManager.put(uniqePlayerID, Dealer);
            IdToGame.put(uniqePlayerID, GameName);
        }
        return GameName;
    }
    
    public static void Playeraction(int playerId, int eventId, ws.blackjack.Action action, float money, int bet) throws InvalidParameters_Exception
    {
        if(!playerManager.containsKey(playerId))
        {
            ThrowInvalidParameter("Error - player doesn`t exist");  
        }
        GameManager Game = gamemanager.get(IdToGame.get(playerId));
        if(Game.GetEvents().size() != eventId)
        {
           ThrowInvalidParameter("Error - event id not last");
        }
        
        Event playerAction = new Event();
        playerAction.setType(EventType.USER_ACTION);
        playerAction.setId(Game.GetEvents().size()+1);
        playerAction.setPlayerName(playerManager.get(playerId).getName());
        playerAction.setPlayerAction(action);
        Game.GetEvents().add(playerAction);
        
        
        if (action.equals(Action.PLACE_BET)){
            Money = (double) money;
        }
        
        plPlayerAction = action;
        synchronized(StopWait)
        {
            StopWait.notifyAll();
        }
        synchronized(Game.getEngine().isWaitEnd())    
        {
            try 
            {
                Game.getEngine().isWaitEnd().wait();
            }
            catch (InterruptedException ex) 
            {
                ThrowInvalidParameter("Action chosen was not valid");
            }
            if(Game.getEngine().isErrorFound())
            {
                ThrowInvalidParameter("Action chosen was not valid");
            }
        }
    }
    
    
}
