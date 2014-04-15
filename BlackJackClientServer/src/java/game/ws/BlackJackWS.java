/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import javax.jws.WebService;

/**
 *
 * @author Yakir
 */
@WebService(serviceName = "BlackJackWebService", portName = "BlackJackWebServicePort", endpointInterface = "ws.blackjack.BlackJackWebService", targetNamespace = "http://blackjack.ws/", wsdlLocation = "WEB-INF/wsdl/BlackJackWS/BlackJackWebService.wsdl")
public class BlackJackWS {

    public java.util.List<ws.blackjack.Event> getEvents(int playerId, int eventId) throws ws.blackjack.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void createGame(java.lang.String name, int humanPlayers, int computerizedPlayers) throws ws.blackjack.DuplicateGameName_Exception, ws.blackjack.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int joinGame(java.lang.String gameName, float money) throws ws.blackjack.InvalidParameters_Exception, ws.blackjack.GameDoesNotExists_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void playerAction(int playerId, int eventId, ws.blackjack.Action action, float bet) throws ws.blackjack.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void resign(int playerId) throws ws.blackjack.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.lang.String createGameFromXML(java.lang.String xmlData) throws ws.blackjack.DuplicateGameName_Exception, ws.blackjack.InvalidParameters_Exception, ws.blackjack.InvalidXML_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public ws.blackjack.GameDetails getGameDetails(java.lang.String gameName) throws ws.blackjack.GameDoesNotExists_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<java.lang.String> getWaitingGames() {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<java.lang.String> getActiveGames() {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public java.util.List<ws.blackjack.PlayerDetails> getPlayersDetails(java.lang.String gameName) throws ws.blackjack.GameDoesNotExists_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public ws.blackjack.PlayerDetails getPlayerDetails(java.lang.String gameName, int playerId) throws ws.blackjack.GameDoesNotExists_Exception, ws.blackjack.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
