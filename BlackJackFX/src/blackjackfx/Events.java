/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.Bid;
import EngineLogic.Communicable;
import EngineLogic.GameEngine;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.Player;
import blackjackfx.Controllers.GameScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

/**
 *
 * @author yakir
 */
public class Events extends Thread implements Communicable
{
    private GameEngine BJGame;
    private GameScreenController scControoler;
    
    public Events(GameEngine BJGame,GameScreenController Controller)
    {
        this.BJGame = BJGame;
        this.scControoler = Controller;
    }
    
    @Override
    public boolean DoesPlayerContinue(Player player) 
    {
        return false;
    }
    
    @Override
    public void run() 
    {
        try 
        {
            BJGame.StartGame(this);
        }
        catch (JAXBException ex) 
        {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) 
        {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    @Override
    public String getFilePathForSave() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlayerAction GetWantedAction() 
    {
        try 
        {
            synchronized(scControoler.getPlayerActionType())
            {
                scControoler.ShowActions();
                System.out.println("1");

                scControoler.getPlayerActionType().wait();
                System.out.println("2");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("3");
        }
        return scControoler.getPlayerActionType().get();
    }

    @Override
    public void PrintPlayerInfo(Player PlayerToPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintBasicPlayerInfo(Player PlayerToPrint) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RoundAction GetFinishRoundAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double GetBidForPlayer(Player BettingPlayer) {
        return 100.0;
    }

    @Override
    public void PrintBidInfo(Bid BidForPrint) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintHandInfo(Hand HandForPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintParticipnatName(GameParticipant PartToPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintMessage(String Message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
