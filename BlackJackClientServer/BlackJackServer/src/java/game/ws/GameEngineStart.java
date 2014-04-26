/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import EngineLogic.Bid;
import EngineLogic.Communicable;
import EngineLogic.CompPlayer;
import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.GameEngine;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.HumanPlayer;
import EngineLogic.Player;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ws.blackjack.Event;
import ws.blackjack.EventType;

/**
 *
 * @author Yakir
 */
public class GameEngineStart extends Thread implements Communicable
{
    private GameEngine GameEngMang;
    
    
    
    public GameEngineStart()
    {
        GameEngMang = new GameEngine();
    }
    
    public GameEngineStart(String File) throws DuplicateCardException, SAXException, TooManyPlayersException, JAXBException
    {
        GameEngMang = new  GameEngine(File);        
    }

    public int GetHumanPlayers()
    {
        int counter = 0;
        for (Player curr : GameEngMang.getGamePlayers()) 
        {
            if(curr instanceof HumanPlayer)
                counter++;
        }
        return counter;
    }
    public int GetCompPlayers()
    {
        int counter = 0;
        for (Player curr : GameEngMang.getGamePlayers()) 
        {
            if(curr instanceof CompPlayer)
                counter++;
        }
        return counter;
    }
    
    public String GetGameName()
    {
        return GameEngMang.getGameName();
    }
    
    public void AddHumanPlayers(String Name) throws TooManyPlayersException
    {
        GameEngMang.AddPlayer(Name);
    }
    
    public void AddCompPlayers(int nNumberComps) throws TooManyPlayersException
    {
        for (int i = 0; i < nNumberComps; i++) 
        {
            GameEngMang.AddPlayer();
        }
    }
    
    @Override
    public void run() 
    {
        try 
        {
            GameEngMang.StartGame(this);
        }
        catch (JAXBException ex) 
        {
            Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) 
        {
            Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    public void PrintAllPlayers(ArrayList<Player> GamePlayers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void RemovePlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintPlayerMessage(GameParticipant ParPlayer, String Message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean DoesPlayerContinue(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String GetFilePathForSave() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlayerAction GetWantedAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintPlayerInfo(Player PlayerToPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintBasicPlayerInfo(Player PlayerToPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RoundAction GetFinishRoundAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double GetBidForPlayer(Player BettingPlayer) 
    {
        Event envtBid = new Event();
        envtBid.setId(EngineManager.getUniqeEventID());
        envtBid.setPlayerName(BettingPlayer.getName());
        envtBid.setType(EventType.NEW_ROUND);
        EngineManager.getEvents().add(envtBid);
        /*try 
        {
            EngineManager.getMoney().wait();
        }
        catch (InterruptedException ex) 
        {
            Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return 50.0;
    }

    @Override
    public void PrintBidInfo(Bid BidForPrint, Player PlayerBid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintHandInfo(Hand HandForPrint, GameParticipant ParPlayer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintMessage(String Message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
