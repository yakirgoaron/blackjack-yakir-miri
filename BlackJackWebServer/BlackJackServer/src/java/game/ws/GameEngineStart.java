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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private Boolean ErrorFound = false;
    private final Boolean WaitEnd = true;
    private Boolean isInEndRound = false;
    private PlayerDetails CurrPlayer;
    private final int Timeout = 40000;
    private ArrayList<Event> Events;

    
    public GameEngineStart()
    {
        GameEngMang = new GameEngine();
        PlayerByName = new HashMap<>();
        Events = new ArrayList<>();
    }
    
    public GameEngineStart(String File) throws DuplicateCardException, SAXException, TooManyPlayersException, JAXBException
    {
        GameEngMang = new  GameEngine(File);  
        PlayerByName = new HashMap<>();
        Events = new ArrayList<>();
    }

    public PlayerDetails getCurrPlayer() {
        return CurrPlayer;
    }
    
    public ArrayList<Event> getEvents() {
        return Events;
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
            playerDetails.getFirstBet().clear();
            playerDetails.setSecondBetWage(0);
            playerDetails.getSecondBet().clear();           
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
        for (Iterator<Player> it = GamePlayers.iterator(); it.hasNext();) {
            Player player = it.next();
            if(PlayerByName.get(player.getName()).getStatus().equals(PlayerStatus.RETIRED))
            {
                it.remove();
                continue;
            }
            SynchronyizePlayerToPlayerDetails(player);
            Event envtBid = new Event();
            envtBid.setId(Events.size()+1);
            envtBid.setPlayerName(player.getName());
            envtBid.setType(EventType.CARDS_DEALT);
            envtBid.setMoney(player.getBids().get(0).getTotalBid().floatValue());
            envtBid.getCards().addAll(ConvertCardsFromEngineToWSDL(player.getBids().get(0).getCards()));
            Events.add(envtBid);
        }
    }

    @Override
    public void RemovePlayer(Player player) {
        Event evntPlayerResign = new Event();
        evntPlayerResign.setId(Events.size()+1);
        evntPlayerResign.setType(EventType.PLAYER_RESIGNED);
        evntPlayerResign.setPlayerName(player.getName());
        Events.add(evntPlayerResign);
    }

    
    @Override
    public void DoesPlayerContinue(Player player) throws PlayerResigned{
        if(PlayerByName.get(player.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
        if(!isInEndRound)
        {
            isInEndRound = true;
            try {
                Thread.sleep(8000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Event evntNewRound = new Event();
        evntNewRound.setId(Events.size()+1);
        evntNewRound.setType(EventType.NEW_ROUND);
        evntNewRound.setPlayerName(player.getName());
        Events.add(evntNewRound);
    }

    @Override
    public String GetFilePathForSave() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlayerAction GetWantedAction(Player CurrentPlayer) throws PlayerResigned {
        
        SynchronyizePlayerToPlayerDetails(CurrentPlayer);
        
        if(PlayerByName.get(CurrentPlayer.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
        CurrPlayer = PlayerByName.get(CurrentPlayer.getName());
        Event envtBid = new Event();
        envtBid.setId(Events.size()+1);
        envtBid.setPlayerName(CurrentPlayer.getName());
        envtBid.setType(EventType.PROMPT_PLAYER_TO_TAKE_ACTION);
        envtBid.setTimeout(Timeout);
        Events.add(envtBid);
        Action act = EngineManager.getPlPlayerAction();
        synchronized(EngineManager.isStopWait())
        {
            try 
            {
                if(act == null)
                    EngineManager.isStopWait().wait(Timeout);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
            }
            act = EngineManager.getPlPlayerAction();
            
            if (act == null){
                RemovePlayer(CurrentPlayer);
                throw new PlayerResigned();
            }
        }
        EngineManager.setPlPlayerAction(null);
        return PlayerAction.valueOf(act.name());
    }

    @Override
    public void PrintPlayerInfo(Player PlayerToPrint) throws PlayerResigned{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintBasicPlayerInfo(Player PlayerToPrint) throws PlayerResigned
    {
        SynchronyizePlayerToPlayerDetails(PlayerToPrint);
        
        if(PlayerByName.get(PlayerToPrint.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
        
        Event envtBid = new Event();
        envtBid.setId(Events.size()+1);
        envtBid.setPlayerName(PlayerToPrint.getName());
        envtBid.setType(EventType.PLAYER_TURN);
        Events.add(envtBid);
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
        CurrPlayer = PlayerByName.get(BettingPlayer.getName());
        Event envtBid = new Event();
        envtBid.setId(Events.size()+1);
        envtBid.setPlayerName(BettingPlayer.getName());
        envtBid.setType(EventType.PROMPT_PLAYER_TO_TAKE_ACTION);
        envtBid.setTimeout(Timeout);
        Events.add(envtBid);
        Double Money = EngineManager.getMoney();
        synchronized(EngineManager.isStopWait())
        {
            try 
            {
                if(Money == null)
                    EngineManager.isStopWait().wait(Timeout);
            }
            catch (InterruptedException ex) 
            {
                Logger.getLogger(GameEngineStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Money = EngineManager.getMoney();
        
        if (Money == null){
            RemovePlayer(BettingPlayer);
            throw new PlayerResigned();           
        }
            
        EngineManager.setMoney(null);
        EngineManager.setPlPlayerAction(null);
        if(PlayerByName.get(BettingPlayer.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
        return Money;
    }
    
    private List<ws.blackjack.Card> ConvertCardsFromEngineToWSDL(List<Card> Cards)
    {
        List<ws.blackjack.Card> newlist = new ArrayList<>();
        for (Card curr : Cards) 
        {
            ws.blackjack.Card newcard = new ws.blackjack.Card();
            newcard.setRank(Rank.valueOf(curr.getRank().name()));
            newcard.setSuit(Suit.valueOf(curr.getSuit().name()));
            newlist.add(newcard);
        }
        return newlist;
    }
    
    @Override
    public void PrintBidInfo(Bid BidForPrint, Player PlayerBid) throws PlayerResigned{
        SynchronyizePlayerToPlayerDetails(PlayerBid);
        
        if(PlayerByName.get(PlayerBid.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
        
        Event envtBid = new Event();
        envtBid.setId(Events.size()+1);
        envtBid.setPlayerName(PlayerBid.getName());
        envtBid.setType(EventType.CARDS_DEALT);
        envtBid.setMoney(BidForPrint.getTotalBid().floatValue());
        envtBid.getCards().addAll(ConvertCardsFromEngineToWSDL(BidForPrint.getCards()));
        
        Events.add(envtBid);
    }

    @Override
    public void PrintHandInfo(Hand HandForPrint, GameParticipant ParPlayer) throws PlayerResigned{
        SynchronyizeHandToPlayerDetails(HandForPrint, ParPlayer);
        isInEndRound = false;
        if(PlayerByName.get(ParPlayer.getName()).getStatus().equals(PlayerStatus.RETIRED))
            throw new PlayerResigned();
                
        Event CardsDealt = new Event();
        CardsDealt.setId(Events.size()+1);
        CardsDealt.setPlayerName(ParPlayer.getName());
        CardsDealt.setType(EventType.CARDS_DEALT);
        CardsDealt.getCards().addAll(ConvertCardsFromEngineToWSDL(HandForPrint.getCards()));
        Events.add(CardsDealt);
    }

    @Override
    public void PrintMessage(String Message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintPlayerAction(AIPlayer PlayerAct, PlayerAction Action) 
    {
        Event evntAction = new Event();
        evntAction.setId(Events.size()+1);
        evntAction.setPlayerName(PlayerAct.getName());
        evntAction.setType(EventType.USER_ACTION);
        evntAction.setPlayerAction(synActionToAction(Action));
        Events.add(evntAction);
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
        evntWinner.setId(Events.size()+1);
        evntWinner.setPlayerName(PlayerWin.getName());
        evntWinner.setType(EventType.GAME_WINNER);
        Events.add(evntWinner);
    }

    @Override
    public void GameEnded() {
        Event evntGameEnded = new Event();
        evntGameEnded.setId(Events.size()+1);
        evntGameEnded.setType(EventType.GAME_OVER);
        Events.add(evntGameEnded);
        //EngineManager.ClearData();
    }

    @Override
    public void ActionOK() 
    {
        synchronized(WaitEnd)
        {            
            ErrorFound = Boolean.FALSE;
            WaitEnd.notifyAll();
        }
    }

    
    @Override
    public void ActionError(String ex) 
    {
        synchronized(WaitEnd)
        {           
            ErrorFound = Boolean.TRUE;
            WaitEnd.notifyAll();
        }
    }

    public Boolean isErrorFound() {
        return ErrorFound;
    }


    public Boolean isWaitEnd() {
        return WaitEnd;
    }
  
}
