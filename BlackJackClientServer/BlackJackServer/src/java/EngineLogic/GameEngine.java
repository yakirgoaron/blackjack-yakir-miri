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
import EngineLogic.Exception.TooLowPlayers;
import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.XmlClasses.Blackjack;
import EngineLogic.XmlClasses.Players;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author yakir
 */
public class GameEngine 
{
    private ArrayList<Player> GamePlayers;
    private ArrayList<Card> GameDeck;
    private Dealer GameDealer;
    private Boolean IsInRound;
    private final int NUMBER_PLAYERS = 6;
    private final int NUMBER_OF_DECKS = 6;
    private String GameName;

    public final static int BLACKJACK = 21;
    
    private void IniGameEngine()
    {
        GamePlayers = new ArrayList<>();
        IsInRound = false;
        CreateDeck();
    }
    
    private void XMLJAXRead(String FileName)throws JAXBException,
                                              TooManyPlayersException,
                                              DuplicateCardException,
                                              SAXException
    {
        JAXBContext JaxReader = JAXBContext.newInstance(Blackjack.class);
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        Schema schema = sf.newSchema(new File(GameEngine.class.getResource("blackjack.xsd").getPath()));
        Unmarshaller XmlParser = JaxReader.createUnmarshaller();
        XmlParser.setSchema(schema);
        InputStream InputFile = new ByteArrayInputStream(FileName.getBytes());
        Blackjack BlackJackGame = (Blackjack) XmlParser.unmarshal(InputFile);
        GameName = BlackJackGame.getName();
        CreatePlayers(BlackJackGame.getPlayers());
        GameDealer = XmlHandler.CreateDealer(BlackJackGame.getDealer(), this);
    }
    
    private void XMLJAXBWrite(String FileName) throws JAXBException, SAXException
    {
        JAXBContext JaxWriter = JAXBContext.newInstance(Blackjack.class);
        Marshaller XmlParser = JaxWriter.createMarshaller();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
        Schema schema = sf.newSchema(new File("blackjack.xsd"));
        XmlParser.setSchema(schema);
        File XmlFile = new File(FileName);
        Blackjack XmlBj = new Blackjack();
        XmlBj.setName("Game");
        XmlBj.setDealer(XmlHandler.SaveDealer(GameDealer));
        Players XmlGamePlayers = new Players();
        
         for (Player player: GamePlayers){
            XmlGamePlayers.getPlayer().add(XmlHandler.SavePlayer(player));
        }
        XmlBj.setPlayers(XmlGamePlayers);
        XmlParser.marshal(XmlBj, XmlFile);
        
       
    }
    
    public GameEngine()
    {
        GameDealer = new Dealer();
        GameName = "Game";
        IniGameEngine();
    }
    
    
    
    public GameEngine(String FileName) throws JAXBException,
                                              TooManyPlayersException,
                                              DuplicateCardException,
                                              SAXException
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
           case STAND:
               break;
        }
    }
    
    private void MakePlayerMove(Communicable commInterface,Bid CurrentBid,Player CurrentPlayer)
    {
        PlayerAction EnumAction = PlayerAction.HIT; 
        while (!EnumAction.equals(PlayerAction.STAND) &&
                !EnumAction.equals(PlayerAction.DOUBLE) &&
                CurrentBid.getSumCards() <= BLACKJACK)
        {
            commInterface.PrintBidInfo(CurrentBid,CurrentPlayer);
            EnumAction = commInterface.GetWantedAction(CurrentPlayer);    
            
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
                commInterface.PrintMessage(ex.toString());
            }
        }  
        commInterface.PrintBidInfo(CurrentBid,CurrentPlayer);
        if(CurrentBid.getSumCards() > BLACKJACK)
        {
            commInterface.PrintHandInfo(CurrentBid,CurrentPlayer);
        }
    }
    
    private void HandleAIPlayers(AIPlayer CurrPlayer,Hand CurrBid,Communicable commInterface)
    {
        try
        {
            PlayerAction actToDo;
            do            
            {
                actToDo = CurrPlayer.Play(CurrBid);
                commInterface.PrintPlayerAction(CurrPlayer, actToDo);
                DoPlayerMove(actToDo,CurrPlayer,CurrBid);
                commInterface.PrintHandInfo(CurrBid,CurrPlayer);
            }while (!actToDo.equals(PlayerAction.STAND));
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
            for (int i = 0; i < player.getBids().size(); i++)                
            {     
                if(player instanceof CompPlayer)
                    HandleAIPlayers((CompPlayer)player,player.getBids().get(i),commInterface); 
                else
                    MakePlayerMove(commInterface,player.getBids().get(i),player);
            }
        }
        HandleAIPlayers(GameDealer,GameDealer.getDealerCards(),commInterface);
    }
    
    private void RemovePlayerWithNoMoney(Communicable commInterface)
    {
        ArrayList<Player> SurvivedPlayers = new ArrayList<>();
        
        for (Player player : GamePlayers) 
        {
            if(player.getMoney() > 0)           
                SurvivedPlayers.add(player);           
            else
                commInterface.RemovePlayer(player);
        }
        GamePlayers = SurvivedPlayers;
        
        
    }
    
    
    public void StartGame(Communicable commInterface) throws JAXBException, SAXException
    {
        if(GamePlayers.isEmpty())
            throw new TooLowPlayers();
        RoundAction NewRoundAction = RoundAction.CONTINUE_GAME;
        
        if (!IsInRound)
            InitAndDealCards(commInterface);
                    
        while(!NewRoundAction.equals(RoundAction.EXIT_GAME))
        {
            commInterface.PrintAllPlayers(GamePlayers);
            HandleRoundPlay(commInterface);
            EndRound(commInterface);
            if(GamePlayers.isEmpty())
                break;
            InitAndDealCards(commInterface);
            NewRoundAction = commInterface.GetFinishRoundAction();
            if(NewRoundAction.equals(RoundAction.SAVE_GAME))
                XMLJAXBWrite(commInterface.GetFilePathForSave());
        }
        
        if (GamePlayers.isEmpty())
            commInterface.GameEnded();
    }
    
    private void EndRound(Communicable commInterface)
    {        
        for (Player player : GamePlayers) 
        {
           player.HandleEndOfRound(commInterface, GameDealer.getSumofCards());
           commInterface.PrintBasicPlayerInfo(player);
        }
        IsInRound = false;
        GameDealer.HandleEndOfRound();
        RemovePlayerWithNoMoney(commInterface);
        PlayerContinueGame(commInterface);
    }
    
    private void PlayerContinueGame(Communicable commInterface)
    {
        for (Iterator<Player> it = GamePlayers.iterator(); it.hasNext();) 
        {
            Player player = it.next();
            if(player instanceof HumanPlayer)               
            {
                commInterface.DoesPlayerContinue(player);
            }               
            
        }
    }
    
    private void InitAndDealCards(Communicable commInterface){
        InsertBidForRound(commInterface);
        StartNewRound();  
        
    }
    
    public Dealer getGameDealer() {
        return GameDealer;
    }
    
    public ArrayList<Player> getGamePlayers() 
    {   
        return GamePlayers;
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
    
    
    public String getGameName() {
        return GameName;
    }
    
}
