/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ws;

import EngineLogic.AIPlayer;
import EngineLogic.Bid;
import EngineLogic.Card;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import ws.blackjack.Event;
import ws.blackjack.EventType;
import ws.blackjack.PlayerDetails;
import ws.blackjack.Rank;
import ws.blackjack.Suit;

/**
 *
 * @author Yakir
 */
public class GameEngineStart extends Thread implements Communicable
{
    private GameEngine GameEngMang;
    private HashMap<String, PlayerDetails> PlayerByName ;
    
    
    public GameEngineStart()
    {
        GameEngMang = new GameEngine();
        PlayerByName = new HashMap<>();
    }
    
    public GameEngineStart(String File) throws DuplicateCardException, SAXException, TooManyPlayersException, JAXBException
    {
        GameEngMang = new  GameEngine(File);  
        PlayerByName = new HashMap<>();
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
    
    private void SynchronyizeContiner(Player currentPlayer)
    {
        if(!PlayerByName.containsKey(currentPlayer.getName()))
        {   
            HashMap<Integer, PlayerDetails> playerManager = EngineManager.getPlayerManager();
            for (Map.Entry<Integer, PlayerDetails> entry : playerManager.entrySet()) 
            {
                PlayerDetails playerDetails = entry.getValue();
                if(playerDetails.getName().equals(currentPlayer.getName()))
                {
                    PlayerByName.put(currentPlayer.getName(), playerDetails);
                }                
            }
        }
    }
    
    private void SynchronyizeBidAndCards(Bid current,List<ws.blackjack.Card> Cards)
    {
        for (Card currCard : current.getCards()) 
        {
            ws.blackjack.Card newcd = new ws.blackjack.Card();
            newcd.setRank(Rank.valueOf(currCard.getRank().name()));
            newcd.setSuit(Suit.valueOf(currCard.getSuit().name()));
            Cards.add(newcd);
        }
    }
    
    private void SynchronyizePlayerToPlayerDetails(Player currentPlayer)
    {
        SynchronyizeContiner(currentPlayer);
        PlayerDetails playerDetails = PlayerByName.get(currentPlayer.getName());
        SynchronyizeBidAndCards(currentPlayer.getBids().get(0),playerDetails.getFirstBet());
        playerDetails.setFirstBetWage(currentPlayer.getBids().get(0).getTotalBid().floatValue());
        if(currentPlayer.getBids().size() > 1)
        {
            SynchronyizeBidAndCards(currentPlayer.getBids().get(1),playerDetails.getSecondBet());
            playerDetails.setSecondBetWage(currentPlayer.getBids().get(1).getTotalBid().floatValue());
        }
        
        
    }
    
    @Override
    public void PrintAllPlayers(ArrayList<Player> GamePlayers) 
    {
        for (Player player : GamePlayers) 
        {
            SynchronyizePlayerToPlayerDetails(player);
            Event envtBid = new Event();
            envtBid.setId(EngineManager.getUniqeEventID());
            envtBid.setPlayerName(player.getName());
            envtBid.setType(EventType.CARDS_DEALT);
            EngineManager.getEvents().add(envtBid);
        }
    }

    @Override
    public void RemovePlayer(Player player) {
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
    public void PrintBasicPlayerInfo(Player PlayerToPrint) 
    {
        SynchronyizePlayerToPlayerDetails(PlayerToPrint);
        Event envtBid = new Event();
        envtBid.setId(EngineManager.getUniqeEventID());
        envtBid.setPlayerName(PlayerToPrint.getName());
        envtBid.setType(EventType.PLAYER_TURN);
        EngineManager.getEvents().add(envtBid);
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

    @Override
    public void PrintPlayerAction(AIPlayer PlayerAct, PlayerAction Action) 
    {
        Event evntAction = new Event();
        evntAction.setId(EngineManager.getUniqeEventID());
        evntAction.setPlayerName(PlayerAct.getName());
        evntAction.setType(EventType.USER_ACTION);
        EngineManager.getEvents().add(evntAction);
        // TODO SEND THE ACTION LOL!!!!!! :D
    }

    @Override
    public void GameWinner(Player PlayerWin) 
    {
        Event evntWinner = new Event();
        evntWinner.setId(EngineManager.getUniqeEventID());
        evntWinner.setPlayerName(PlayerWin.getName());
        evntWinner.setType(EventType.GAME_WINNER);
        EngineManager.getEvents().add(evntWinner);
    }
    
}
