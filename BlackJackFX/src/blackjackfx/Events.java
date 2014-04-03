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
import javafx.application.Platform;
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
    public String getFilePathForSave() 
    {
        synchronized(scControoler.GetPath())
            {
                
                scControoler.GetFilePathToSave();
            try {                
                scControoler.GetPath().wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        return scControoler.GetPath().get();
    }

    @Override
    public PlayerAction GetWantedAction() 
    {
        try 
        {
            synchronized(scControoler.getPlayerActionType())
            {
                scControoler.ShowActions();

                scControoler.getPlayerActionType().wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);

        }
        return scControoler.getPlayerActionType().get();
    }

    @Override
    public void PrintPlayerInfo(Player PlayerToPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintBasicPlayerInfo(final Player PlayerToPrint) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayPlayer(PlayerToPrint);
                                }});
        
    }

    @Override
    public RoundAction GetFinishRoundAction() {
        try 
        {
            synchronized(scControoler.getRoundChoice())
            {
                
                scControoler.ShowRoundActions();
                
                scControoler.getRoundChoice().wait();
                ClearTable();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return scControoler.getRoundChoice().get();
    }

    @Override
    public Double GetBidForPlayer(Player BettingPlayer) {
        return 100.0;
    }

    @Override
    public void PrintBidInfo(final Bid BidForPrint, final Player PlayerBid) {
       Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayBid(BidForPrint, PlayerBid);
                                }}); 
    }

    @Override
    public void PrintHandInfo(final Hand HandForPrint,final GameParticipant ParPlayer) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayHand(HandForPrint, ParPlayer);
                                }}); 
    }

    @Override
    public void PrintParticipnatName(GameParticipant PartToPrint) {
        //todo
    }

    @Override
    public void PrintMessage(final String Message) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.DisplayMessage(Message);
                                }}); 
    }

    private void ClearTable() {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.ClearTable();
                                }});        
    }
    
}
