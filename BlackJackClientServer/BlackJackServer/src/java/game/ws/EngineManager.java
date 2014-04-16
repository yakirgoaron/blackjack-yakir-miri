/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import java.util.HashMap;
import ws.blackjack.DuplicateGameName;
import ws.blackjack.DuplicateGameName_Exception;
import ws.blackjack.GameDetails;
import ws.blackjack.GameDoesNotExists;
import ws.blackjack.GameDoesNotExists_Exception;
import ws.blackjack.GameStatus;
import ws.blackjack.InvalidParameters;
import ws.blackjack.InvalidParameters_Exception;

/**
 *
 * @author Yakir
 */
public class EngineManager {
    static HashMap<String,GameDetails> gamemanager = new HashMap<>();
    
    private EngineManager()
    {
    }
    
    public static void CreateGame(String name, int humanPlayers, int computerizedPlayers) throws DuplicateGameName_Exception, InvalidParameters_Exception
    {
        if(humanPlayers < 0 )
        {
            InvalidParameters info = new InvalidParameters();
            info.setMessage("Need one human player");
            throw new InvalidParameters_Exception("Need one human player",info);
        }
        if((humanPlayers + computerizedPlayers) > 6)
        {
            InvalidParameters info = new InvalidParameters();
            info.setMessage("Too many players");
            throw new InvalidParameters_Exception("Too many players",info);
        }
       
        GameDetails gmDetail = new GameDetails();
        gmDetail.setHumanPlayers(humanPlayers);
        gmDetail.setComputerizedPlayers(computerizedPlayers);
        gmDetail.setJoinedHumanPlayers(0);
        gmDetail.setName(name);
        gmDetail.setStatus(GameStatus.WAITING);
        if(gamemanager.containsKey(name))
        {
            DuplicateGameName faultinfo = new DuplicateGameName();
            faultinfo.setMessage("Error Duplicate name");
            throw new DuplicateGameName_Exception("Error Duplicate name",faultinfo);
        }
        gamemanager.put(name, gmDetail);
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
    
    
    
    
}
