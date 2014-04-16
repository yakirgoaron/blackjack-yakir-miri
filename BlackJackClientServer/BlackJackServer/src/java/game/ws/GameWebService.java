/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import javax.jws.WebService;
import ws.blackjack.DuplicateGameName_Exception;
import ws.blackjack.GameDoesNotExists_Exception;
import ws.blackjack.InvalidParameters_Exception;
import ws.blackjack.InvalidXML_Exception;

/**
 *
 * @author Yakir
 */
@WebService(serviceName = "BlackJackWebService", portName = "BlackJackWebServicePort", endpointInterface = "ws.blackjack.BlackJackWebService", targetNamespace = "http://blackjack.ws/", wsdlLocation = "WEB-INF/wsdl/NewWebServiceFromWSDL/BlackJackWebService.wsdl")
public class GameWebService {

    public java.util.List<ws.blackjack.Event> getEvents(int playerId, int eventId) throws InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void createGame(java.lang.String name, int humanPlayers, int computerizedPlayers) throws InvalidParameters_Exception, DuplicateGameName_Exception {
        EngineManager.CreateGame(name, humanPlayers, computerizedPlayers);
    }

    public int joinGame(java.lang.String gameName, float money) throws GameDoesNotExists_Exception, InvalidParameters_Exception {
        return EngineManager.PlayerJoinGame(gameName, money);
    }

    public void playerAction(int playerId, int eventId, ws.blackjack.Action action, float bet) throws InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void resign(int playerId) throws InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.String createGameFromXML(java.lang.String xmlData) throws InvalidParameters_Exception, InvalidXML_Exception, DuplicateGameName_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public ws.blackjack.GameDetails getGameDetails(java.lang.String gameName) throws GameDoesNotExists_Exception {
        return EngineManager.GetGameDetails(gameName);
    }

    public java.util.List<java.lang.String> getWaitingGames() {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<java.lang.String> getActiveGames() {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<ws.blackjack.PlayerDetails> getPlayersDetails(java.lang.String gameName) throws GameDoesNotExists_Exception {
       return EngineManager.GetGamePlayers(gameName);
    }

    public ws.blackjack.PlayerDetails getPlayerDetails(java.lang.String gameName, int playerId) throws GameDoesNotExists_Exception, InvalidParameters_Exception {
        return EngineManager.GetPlayerDetails(playerId);
    }
    
}
