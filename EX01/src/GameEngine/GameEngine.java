/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameEngine;

import GameEngine.Exception.RoundStartedException;
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
    
    private void ValidateAddPlayerToGame()throws TooManyPlayersException,RoundStartedException
    {
        if(GamePlayers.size() == NUMBER_PLAYERS)
            throw new TooManyPlayersException();

        if(IsInRound)
            throw new RoundStartedException();
    }
    
    public void AddPlayer(String Name) throws TooManyPlayersException,RoundStartedException
    {
        ValidateAddPlayerToGame();
        GamePlayers.add(new HumanPlayer(Name));
    }
    
    public void AddPlayer() throws TooManyPlayersException,RoundStartedException
    {
       ValidateAddPlayerToGame();
       GamePlayers.add(new CompPlayer());
    }
    
    public Player GetCurrentPlayer()
    {
        return GamePlayers.get(PlayerTurn++);
    }
    
    
    
}
