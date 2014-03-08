/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Communicable.PlayerAction;
import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.RulesDosentAllowException;
import EngineLogic.Exception.TooLowMoneyException;
import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.XmlClasses.Blackjack;
import EngineLogic.XmlClasses.Players;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author yakir
 */
public class GameEngine 
{
    private ArrayList<Player> GamePlayers;
    private ArrayList<Card> GameDeck;
    private Dealer GameDealer;
    private int PlayerTurn;
    private Boolean IsInRound;
    private final int NUMBER_PLAYERS = 6;
    private final int NUMBER_OF_DECKS = 6;
    
    private void IniGameEngine()
    {
        GamePlayers = new ArrayList<>();
        IsInRound = false;
        CreateDeck();
    }
    
    private void XMLJAXRead(String FileName)throws JAXBException,
                                              TooManyPlayersException,
                                              DuplicateCardException
    {
        JAXBContext JaxReader = JAXBContext.newInstance(Blackjack.class);
        Unmarshaller XmlParser = JaxReader.createUnmarshaller();
        File XmlFile = new File(FileName);
        Blackjack BlackJackGame = (Blackjack) XmlParser.unmarshal(XmlFile);
        CreatePlayers(BlackJackGame.getPlayers());
        GameDealer = XmlHandler.CreateDealer(BlackJackGame.getDiller(), this);
    }
    
    public GameEngine()
    {
        GameDealer = new Dealer();
        IniGameEngine();
    }
    
    
    
    public GameEngine(String FileName) throws JAXBException,
                                              TooManyPlayersException,
                                              DuplicateCardException
    {
        IniGameEngine();
        XMLJAXRead(FileName);
        IsInRound = true;
    }
    private void CreateDeck()
    {
        GameDeck = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DECKS; i++) 
        {
            GameDeck.addAll(Card.newDeck());
        }
    }
    private void CreatePlayers(Players XmlPlayers) throws TooManyPlayersException, DuplicateCardException 
    {
        for (EngineLogic.XmlClasses.Player player : XmlPlayers.getPlayer()) 
        {   
            ValidateAddPlayerToGame();
            GamePlayers.add(XmlHandler.CreatePlayer(player, this));            
        }
    }
     
    private void StartNewRound()
    {
        if(!IsInRound)
        {
            CreateDeck();
            PlayerTurn = 0;
            IsInRound = true;
        }
    }
    
    private void InsertBidForRound(Communicable commGetBid )
    {
        for (Player Participant : GamePlayers) 
        {
            Participant.GivePlayerCards(PullCard(), PullCard(),commGetBid.GetBidForPlayer());
        }
    }
    
    private Card PullCard()
    {
        return GameDeck.remove(0);
    }
    
    private void ValidateAddPlayerToGame()throws TooManyPlayersException
    {
        if(GamePlayers.size() == NUMBER_PLAYERS)
            throw new TooManyPlayersException();
    }
    
    public void AddPlayer(String Name) throws TooManyPlayersException
    {
        ValidateAddPlayerToGame();
        GamePlayers.add(new HumanPlayer(Name));
    }
    
    public void AddPlayer() throws TooManyPlayersException
    {
       ValidateAddPlayerToGame();
       GamePlayers.add(new CompPlayer());
    }
    
    
    private void DoPlayerMove(PlayerAction EnumAction,Player CurrentPlayer,Bid CurrentBid) throws RulesDosentAllowException, TooLowMoneyException
    {
        switch(EnumAction)
        {
           case DOUBLE:
           {
               CurrentPlayer.DoubleBid(CurrentBid, PullCard());
               break;
           }
           case HIT:
           {
               CurrentPlayer.HitBid(CurrentBid, PullCard());
               break;
           }
           case SPLIT:
           {
               CurrentPlayer.Split();
               break;
           }
           case STAY:
               break;
        }
    }
    
    private void MakePlayerMove(Communicable commInterface,Player CurrentPlayer)
    {
        PlayerAction EnumAction; 
        for (Bid CurrBid : CurrentPlayer.getBids()) 
        {
            while (true)
            {
                EnumAction = commInterface.GetWantedAction();    
                commInterface.PrintBidInfo(CurrBid);
                try 
                {
                    DoPlayerMove(EnumAction, CurrentPlayer, CurrBid);
                    break;
                } 
                catch (RulesDosentAllowException ex) 
                {
                    
                }
                catch (TooLowMoneyException ex) 
                {
                    
                }
            }
        }
        
    }
    
    private void HandleRoundPlay(Communicable commInterface)
    {
        for (Player player : GamePlayers)
        {
            commInterface.PrintPlayerInfo(player);
            if(player instanceof CompPlayer)
                ((CompPlayer)player).Play();
            else
                MakePlayerMove(commInterface,player);
        }
    }
    
    public void StartGame(Communicable commInterface)
    {
        InsertBidForRound(commInterface);
        StartNewRound();
        HandleRoundPlay(commInterface);
        //ENDRound
    }
    
    public Dealer getGameDealer() {
        return GameDealer;
    }
    
    public ArrayList<HumanPlayer> GetHumanPlayers() 
    {   
        ArrayList<HumanPlayer> HumanPlayers = new ArrayList<>();
        for (Player player : GamePlayers) 
        {
            if(player instanceof HumanPlayer)
                HumanPlayers.add((HumanPlayer)player);
        }
        return HumanPlayers;
    }
    
    Card getCardAndRemove(Card.Rank CardRank,Card.Suit CardSuit)
    {
        Card RemovedCard = null;
        for (int i = 0; i < GameDeck.size(); i++) 
        {
            if(GameDeck.get(i).getRank().compareTo(CardRank) == 0 &&
               GameDeck.get(i).getSuit().compareTo(CardSuit) == 0)
            {
                RemovedCard = GameDeck.remove(i);
                break;
            }
        }
        return RemovedCard;
    }
    
    
    public Boolean GetIsInRound() {
        return IsInRound;
    }
    
}
