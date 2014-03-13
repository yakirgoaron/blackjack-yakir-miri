/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Communicable.PlayerAction;
import EngineLogic.Communicable.RoundAction;
import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.RulesDosentAllowException;
import EngineLogic.Exception.TooLowMoneyException;
import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.XmlClasses.Blackjack;
import EngineLogic.XmlClasses.Players;
import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
    
    private void XMLJAXBWrite(String FileName) throws JAXBException
    {
        JAXBContext JaxWriter = JAXBContext.newInstance(Blackjack.class);
        Marshaller XmlParser = JaxWriter.createMarshaller();
        File XmlFile = new File(FileName);
        XmlParser.marshal(GameDealer, XmlFile);
        
        for (Player player: GamePlayers){
            XmlParser.marshal(player, XmlFile);
        }
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
        for (Player player : GamePlayers) 
        {
            player.GivePlayerCards(PullCard(), 
                                   PullCard(),
                                   player.GetBidForPlayer(commGetBid));
        }
        GameDealer.HitBid(GameDealer.getDealerCards(), PullCard());
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
    
    
    private void DoPlayerMove(PlayerAction EnumAction,
                              GameParticipant CurrentPlayer,
                              Hand CurrentBid) 
            throws RulesDosentAllowException, TooLowMoneyException
    {
        switch(EnumAction)
        {
           case DOUBLE:
           {
               if (CurrentBid instanceof Bid)                 
                   CurrentPlayer.DoubleBid((Bid)CurrentBid, PullCard());
               else throw new RulesDosentAllowException(
                       "You are not allowed to choose double bid action");
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
    
    private void MakePlayerMove(Communicable commInterface,Bid CurrentBid,Player CurrentPlayer)
    {
        PlayerAction EnumAction = PlayerAction.DOUBLE; 
        while (!EnumAction.equals(PlayerAction.STAY) &&
                CurrentBid.getSumCards() < 22)
        {
            commInterface.PrintBidInfo(CurrentBid);
            EnumAction = commInterface.GetWantedAction();    
            
            try 
            {
                DoPlayerMove(EnumAction, CurrentPlayer, CurrentBid);
            } 
            catch (RulesDosentAllowException ex) 
            {
                commInterface.PrintMessage(ex.getMessage());
            }
            catch (TooLowMoneyException ex) 
            {
                commInterface.PrintMessage(ex.getMessage());
            }
        }  
        if(CurrentBid.getSumCards() > 21)
            commInterface.PrintMessage("YOU ARE BURNED");
    }
    
    private void HandleAIPlayers(AIPlayer CurrPlayer,Hand CurrBid,Communicable commInterface)
    {
        try
        {
            PlayerAction actToDo;
            do            
            {
                actToDo = CurrPlayer.Play(CurrBid);
                commInterface.PrintHandInfo(CurrBid);
                commInterface.PrintMessage(actToDo.getDescription());
                DoPlayerMove(actToDo,CurrPlayer,CurrBid);
                commInterface.PrintHandInfo(CurrBid);
            }while (!actToDo.equals(PlayerAction.STAY));
        } 
        catch (RulesDosentAllowException ex) {
        } catch (TooLowMoneyException ex) {
        }
    }
    
    private void HandleRoundPlay(Communicable commInterface) 
    {
        for (Player player : GamePlayers)
        {
            commInterface.PrintBasicPlayerInfo(player);
            for (Bid CurrBid : player.getBids()) 
            {     
                if(player instanceof CompPlayer)
                    HandleAIPlayers((CompPlayer)player,CurrBid,commInterface); 
                else
                    MakePlayerMove(commInterface,CurrBid,player);
            }
        }
        commInterface.PrintParticipnatName(GameDealer);
        HandleAIPlayers(GameDealer,GameDealer.getDealerCards(),commInterface);
    }
    
    public void StartGame(Communicable commInterface) throws JAXBException
    {
        RoundAction NewRoundAction ;
        do
        {
            if (!IsInRound)
                InitAndDealCards(commInterface);
            HandleRoundPlay(commInterface);
            EndRound(commInterface);
            NewRoundAction = commInterface.GetFinishRoundAction();
        }while(!NewRoundAction.equals(RoundAction.SAVE_GAME));
        XMLJAXBWrite(commInterface.EnterFileNameForSave());
    }
    private void EndRound(Communicable commInterface)
    {
        commInterface.PrintMessage("******ROUND ENDED AND SCOURE IS********");
        
        for (Player player : GamePlayers) 
        {
           player.HandleEndOfRound(GameDealer.getSumofCards());
           commInterface.PrintBasicPlayerInfo(player);
        }
        IsInRound = false;
        GameDealer.HandleEndOfRound();
    }
    private void InitAndDealCards(Communicable commInterface){
        InsertBidForRound(commInterface);
        StartNewRound();  
        
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
