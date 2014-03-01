/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameEngine;

import GameEngine.Exception.TooManyPlayersException;
import GameEngine.XmlClasses.Blackjack;
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
        return GameDeck.get(TopDeckCard++);
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
    
    public void LoadFromXml(String FileName) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(Blackjack.class);
        Unmarshaller u = jc.createUnmarshaller();
        File f = new File(FileName);
        Blackjack product = (Blackjack) u.unmarshal(f);
        product.getPlayers();
        product.getDiller();
        
    }
}
