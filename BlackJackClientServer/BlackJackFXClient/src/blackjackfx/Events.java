/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.Bid;
import EngineLogic.Communicable;
import EngineLogic.CompPlayer;
import EngineLogic.Dealer;
import EngineLogic.GameEngine;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.Player;
import GameEnums.SaveOptions;
import blackjackfx.Controllers.GameScreenController;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
    private SimpleBooleanProperty GameEnded;
    private String FilePath;
    
    public Events(GameEngine BJGame,GameScreenController Controller)
    {
        GameEnded = new SimpleBooleanProperty(false);
        this.BJGame = BJGame;
        this.scControoler = Controller;
    }

    public SimpleBooleanProperty getGameEnded() {
        return GameEnded;
    }
    
    @Override
    public boolean DoesPlayerContinue(final Player player) 
    {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.ShowPlayerContGame(player.getName());
                                }});      
         try 
        {
            synchronized(scControoler.getDoesPlayerContinue())
            {               

                scControoler.getDoesPlayerContinue().wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
         
       return scControoler.getDoesPlayerContinue().get();
       
    }
    
    @Override
    public void run() 
    {
        try 
        {
            BJGame.StartGame(this);
            GameEnded.set(true);
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
    public String GetFilePathForSave() 
    {
       SaveOptions UserChoice;
        
        if (FilePath == null){
            scControoler.GetFilePathToSave();
            FilePath = scControoler.GetPath().get();
        }
        else {
            UserChoice = SaveOrSaveAs();
            
            if (UserChoice.equals(SaveOptions.SAVE_AS)){
                scControoler.GetFilePathToSave();
                FilePath = scControoler.GetPath().get();
            }           
        }       
        return FilePath;
    }
    
    private SaveOptions SaveOrSaveAs() {       
        
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    scControoler.ShowSaveOptions();
                                }});    
        
        try 
        {
            synchronized(scControoler.getPlayerSaveType())
            {               
                scControoler.getPlayerSaveType().wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);

        }
        return scControoler.getPlayerSaveType().get();
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
       
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                      scControoler.GetHideBidWindow().set(true);
                                }});
        
        try 
        {
            synchronized(scControoler.getRoundChoice())
            {
                ClearTable();
                scControoler.ShowRoundActions();
                
                scControoler.getRoundChoice().wait();
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return scControoler.getRoundChoice().get();
    }

    @Override
    public Double GetBidForPlayer(final Player BettingPlayer) {
        
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                      ScreenManager.GetInstance().getBidScCr().SetPlayer(BettingPlayer);
                                      scControoler.GetHideBidWindow().set(false);
                                }});       
        
        try 
        {
            synchronized(ScreenManager.GetInstance().getBidScCr().GetNumberBid())
            {
                ScreenManager.GetInstance().getBidScCr().GetNumberBid().wait();
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            
        } 
        
        return ScreenManager.GetInstance().getBidScCr().GetNumberBid().getValue();
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
        try {
            Platform.runLater(new Runnable(){
                @Override
                public void run()
                {
                    scControoler.DisplayHand(HandForPrint, ParPlayer);
                }});
            if(ParPlayer instanceof CompPlayer || ParPlayer instanceof Dealer)
                Thread.sleep(2100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @Override
    public void PrintAllPlayers(final ArrayList<Player> GamePlayers) {
       
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.GetHideBidWindow().set(true);
                                     scControoler.ShowPlayers(GamePlayers);
                                }});
    }

    @Override
    public void RemovePlayer(final Player player) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.RemovePlayer(player);
                                }}); 
    }

    @Override
    public void PrintPlayerMessage(final GameParticipant ParPlayer, final String Message) {
        Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                     scControoler.PrintPlayerMessage(ParPlayer, Message);
                                }});
    }
   
}
