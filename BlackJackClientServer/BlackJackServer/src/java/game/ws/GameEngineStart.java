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
import EngineLogic.Exception.PlayerResigned;
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
import ws.blackjack.Action;
import ws.blackjack.Event;
import ws.blackjack.EventType;
import ws.blackjack.PlayerDetails;
import ws.blackjack.PlayerStatus;
import ws.blackjack.PlayerType;
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
    
    public void SyncAllPlayers()
    {
        for (Player player : GameEngMang.getGamePlayers()) 
        {
            SynchronyizePlayerToPlayerDetails(player);
        }
    }
    
    public void CreatePlayerDetailsEngMng(String GameName)
    {
        for (Player curr : GameEngMang.getGamePlayers())
        {
           PlayerDetails player = new PlayerDetails();
           player.setMoney(curr.getMoney().floatValue());
           if (curr instanceof HumanPlayer)
                   player.setType(PlayerType.HUMAN);
           else
               player.setType(PlayerType.COMPUTER);
           player.setStatus(PlayerStatus.ACTIVE);
           player.setName(curr.getName()); 
           int PlayerID = EngineManager.getUniqePlayerID();
           EngineManager.getPlayerManager().put(PlayerID, player);
           EngineManager.getIdToGame().put(PlayerID, GameName);
        }
        
        
    }
    
    public void AddCompPlayers(int nNumberComps) throws TooManyPlayersException
    {
        for (int i = 0; i < nNumberComps; i++) 
        {
            GameEngMang.AddPlayer();
        }
        SyncAllPlayers();
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
    
    private void SynchronyizeContiner(GameParticipant currentPlayer)
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
    
    private void SynchronyizeHandAndCards(Hand current,List<ws.blackjack.Card> Cards)
    {
        Cards.clear();
        
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
        if(currentPlayer.getBids().size() > 0)
        {
            SynchronyizeHandAndCards(currentPlayer.getBids().get(0),playerDetails.getFirstBet());
            playerDetails.setFirstBetWage(currentPlayer.getBids().get(0).getTotalBid().floatValue());
            if(currentPlayer.getBids().size() > 1)
            {
                SynchronyizeHandAndCards(currentPlayer.getBids().get(1),playerDetails.getSecondBet());
                playerDetails.setSecondBetWage(currentPlayer.getBids().get(1).getTotalBid().floatValue());
            }
        }
        else{
            playerDetails.setFirstBetWage(0);
        }
        playerDetails.setMoney(currentPlayer.getMoney().floatValue());
        
    }
    
    private void SynchronyizeHandToPlayerDetails(Hand HandToSync, 
                                                 GameParticipant ParPlayer){
    
        SynchronyizeContiner(ParPlayer);
        PlayerDetails playerDetails = PlayerByName.get(ParPlayer.getName());
        SynchronyizeHandAndCards(HandToSync, playerDetails.getFirstBet());      
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
        Event evntPlayerResign = new Event();
        evntPlayerResign.setId(EngineManager.getUniqeEventID());
        evntPlayerResign.setType(EventType.PLAYER_RESIGNED);
        evntPlayerResign.setPlayerName(player.getName());
        EngineManager.getEvents().add(evntPlayerResign);
    }

    
    @Override
    public void DoesPlayerContinue(Player player) {
        Event evntNewRound = new Event();
        evntNewRound.setId(EngineManager.getUniqeEventID());
        evntNewRound.setType(EventType.NEW_ROUND);
        evntNewRound.setPlayerName(player.getName());
        EngineManager.getEvents().add(evntNewRound);
    }

    @Override
    public String GetFilePathForSave() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlayerAction GetWantedAction(Player CurrentPlayer) throws PlayerResigned {
        if(PlayerByName.get(CurrentPlayer.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
        SynchronyizePlayerToPlayerDetails(CurrentPlayer);
        Event envtBid = new Event();
        envtBid.setId(EngineManager.getUniqeEventID());
        envtBid.setPlayerName(CurrentPlayer.getName());
        envtBid.setType(EventType.PROMPT_PLAYER_TO_TAKE_ACTION);
        EngineManager.getEvents().add(envtBid);
        synchronized(EngineManager.isStopWait())
        {
            try {
                EngineManager.isStopWait().wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return PlayerAction.valueOf(EngineManager.getPlPlayerAction().name());
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
    public Double GetBidForPlayer(Player BettingPlayer) throws PlayerResigned 
    {
        if(PlayerByName.get(BettingPlayer.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
        Event envtBid = new Event();
        envtBid.setId(EngineManager.getUniqeEventID());
        envtBid.setPlayerName(BettingPlayer.getName());
        envtBid.setType(EventType.PROMPT_PLAYER_TO_TAKE_ACTION);
        EngineManager.getEvents().add(envtBid);
        synchronized(EngineManager.isStopWait())
        {
            try 
            {
               EngineManager.isStopWait().wait();
            }
            catch (InterruptedException ex) 
            {
                Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return EngineManager.getMoney();
    }

    @Override
    public void PrintBidInfo(Bid BidForPrint, Player PlayerBid) {
        SynchronyizePlayerToPlayerDetails(PlayerBid);
        Event envtBid = new Event();
        envtBid.setId(EngineManager.getUniqeEventID());
        envtBid.setPlayerName(PlayerBid.getName());
        envtBid.setType(EventType.CARDS_DEALT);
        EngineManager.getEvents().add(envtBid);
    }

    @Override
    public void PrintHandInfo(Hand HandForPrint, GameParticipant ParPlayer) {
        SynchronyizeHandToPlayerDetails(HandForPrint, ParPlayer);
        Event CardsDealt = new Event();
        CardsDealt.setId(EngineManager.getUniqeEventID());
        CardsDealt.setPlayerName(ParPlayer.getName());
        CardsDealt.setType(EventType.CARDS_DEALT);
        EngineManager.getEvents().add(CardsDealt);
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
        evntAction.setPlayerAction(synActionToAction(Action));
        EngineManager.getEvents().add(evntAction);
        SyncAllPlayers();
        
    }
    
    private ws.blackjack.Action synActionToAction(PlayerAction Action)
    {
      return ws.blackjack.Action.valueOf(Action.name());  
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

    @Override
    public void GameEnded() {
        Event evntGameEnded = new Event();
        evntGameEnded.setId(EngineManager.getUniqeEventID());
        evntGameEnded.setType(EventType.GAME_OVER);
        EngineManager.getEvents().add(evntGameEnded);
    }
    
}
