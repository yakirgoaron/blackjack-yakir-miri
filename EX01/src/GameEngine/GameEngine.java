/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameEngine;

import GameEngine.Exception.TooManyPlayersException;
import java.util.ArrayList;

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
    final int NUMBER_PLAYERS = 6;
    
    
    public GameEngine()
    {
        GamePlayers = new ArrayList<>();
        GameDealer = new Dealer();
        NewRound();
    }
    
    private void NewRound()
    {
        GameDeck = Card.newDeck();
        TopDeckCard = 0;
        PlayerTurn = 0;
    }
    
    
    public void StartNewRound()
    {
        NewRound();
    }
    
    public Card PullCard()
    {
        return GameDeck.get(TopDeckCard++);
    }
    
    
    public void AddPlayer(String Name) throws TooManyPlayersException
    {
        if(GamePlayers.size() == NUMBER_PLAYERS)
        {
            throw new TooManyPlayersException();
        }
        GamePlayers.add(new HumanPlayer());
    }
    
    public void AddPlayer() throws TooManyPlayersException
    {
        if(GamePlayers.size() == NUMBER_PLAYERS)
        {
             throw new TooManyPlayersException();
        }
        GamePlayers.add(new CompPlayer());
    }
    
    public Player GetCurrentPlayer()
    {
        return GamePlayers.get(PlayerTurn++);
    }
    
    
    
}
