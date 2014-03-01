/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.XmlClasses.Blackjack;
import EngineLogic.XmlClasses.Players;
import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author yakir
 */
public class GameEngine 
{
    ArrayList<Player> GamePlayers;
    ArrayList<Card> GameDeck;
    Dealer GameDealer;
    int TopDeckCard;
    int PlayerTurn;
    Boolean IsInRound;
    final int NUMBER_PLAYERS = 6;
    
    
    public GameEngine()
    {
        GamePlayers = new ArrayList<>();
        GameDealer = new Dealer();
        IsInRound = false;
        
    }
        
    public GameEngine(String FileName) throws JAXBException
    {
        JAXBContext JaxReader = JAXBContext.newInstance(Blackjack.class);
        Unmarshaller XmlParser = JaxReader.createUnmarshaller();
        File XmlFile = new File(FileName);
        Blackjack BlackJackGame = (Blackjack) XmlParser.unmarshal(XmlFile);
        CreatePlayers(BlackJackGame.getPlayers());
    }
    
    private void CreatePlayers(Players XmlPlayers)
    {
        for (EngineLogic.XmlClasses.Player player : XmlPlayers.getPlayer()) 
        {
            switch(player.getType())
            {
                case COMPUTER:
                {
                    GamePlayers.add(new CompPlayer(player));
                    break;
                }
                case HUMAN:
                {
                    GamePlayers.add(new HumanPlayer(player));
                    break;
                }
            }
        }
    }
    
    
    public void StartNewRound()
    {
        GameDeck = Card.newDeck();
        TopDeckCard = 0;
        PlayerTurn = 0;
        for (Player player : GamePlayers)
        {
            player.GivePlayerCards(PullCard(), PullCard());
        }
    }
    
    public Card PullCard()
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
    
    public Player GetCurrentPlayer()
    {
        return GamePlayers.get(PlayerTurn++);
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
    
    
}
