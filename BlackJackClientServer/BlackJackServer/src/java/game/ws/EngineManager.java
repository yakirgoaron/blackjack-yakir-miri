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
import ws.blackjack.GameStatus;

/**
 *
 * @author Yakir
 */
public class EngineManager {
    static HashMap<String,GameDetails> gamemanager = new HashMap<>();
    
    private EngineManager()
    {
    }
    
    public static void CreateGame(String name, int humanPlayers, int computerizedPlayers) throws DuplicateGameName_Exception
    {
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
    
    
    
    
    
}
