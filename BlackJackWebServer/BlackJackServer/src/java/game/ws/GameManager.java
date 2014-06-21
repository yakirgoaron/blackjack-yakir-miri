/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.TooManyPlayersException;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ws.blackjack.Event;
import ws.blackjack.GameDetails;

/**
 *
 * @author Yakir
 */
public class GameManager 
{
    private GameEngineStart Engine ;

    
    private GameDetails gmDetails;
  
    public GameManager()
    {
        Engine = new GameEngineStart();
        gmDetails = new GameDetails();
    }
    
    public GameManager(String XMLData) throws DuplicateCardException, SAXException, TooManyPlayersException, JAXBException
    {
        Engine = new GameEngineStart(XMLData);
        gmDetails = new GameDetails();
    }
    
    public ArrayList<Event> GetEvents()
    {
        return Engine.getEvents();
    }       
            
    public GameDetails getGmDetails() {
        return gmDetails;
    }

    public void setGmDetails(GameDetails gmDetails) {
        this.gmDetails = gmDetails;
    }
    
    public GameEngineStart getEngine() {
        return Engine;
    }
}
