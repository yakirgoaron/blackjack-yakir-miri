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
import ws.blackjack.Action;
import ws.blackjack.Event;
import ws.blackjack.GameDetails;

/**
 *
 * @author Yakir
 */
public class GameManager 
{
    private GameEngineStart Engine ;

    private Action plPlayerAction = null;
    private GameDetails gmDetails;
  
    public GameManager()
    {
        Engine = new GameEngineStart();
        Engine.setMyManager(this);
        gmDetails = new GameDetails();
    }
    
    public GameManager(String XMLData) throws DuplicateCardException, SAXException, TooManyPlayersException, JAXBException
    {
        Engine = new GameEngineStart(XMLData);
        Engine.setMyManager(this);
        gmDetails = new GameDetails();
    }
    
    public ArrayList<Event> GetEvents()
    {
        return Engine.getEvents();
    } 
    
    public Action getPlPlayerAction() {
        return plPlayerAction;
    } 
    
    public void setPlPlayerAction(Action plPlayerAction) {
        this.plPlayerAction = plPlayerAction;
        
    }
    public GameDetails getGmDetails() {
        return gmDetails;
    }

    public void setGmDetails(GameDetails gmDetails) {
        this.gmDetails = gmDetails;
        Engine.SetGameName(gmDetails.getName());
    }
    
    public GameEngineStart getEngine() {
        return Engine;
    }
}
