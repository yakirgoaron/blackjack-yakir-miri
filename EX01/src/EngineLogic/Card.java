/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author yakir
 */
public class Card 
{
    public enum Rank {DEUCE {int Value(){return 2;}},
                      THREE {int Value(){return 3;}},
                      FOUR  {int Value(){return 4;}},
                      FIVE  {int Value(){return 5;}},
                      SIX   {int Value(){return 6;}},
                      SEVEN {int Value(){return 7;}},
                      EIGHT {int Value(){return 8;}},
                      NINE  {int Value(){return 9;}},
                      TEN   {int Value(){return 10;}},
                      JACK  {int Value(){return 10;}},
                      QUEEN {int Value(){return 10;}},
                      KING  {int Value(){return 10;}},
                      ACE   {int Value(){return 1;}}}
    public enum Suit {CLUBS, DIAMONDS, HEARTS2, SPADES}
    private final Rank rank;
    private final Suit suit;

    private Card(Rank rank, Suit suit)
    {
        this.rank = rank; 
        this.suit = suit;
    }

    @Override
    public String toString() 
    { 
        return rank + " of " + suit;
    } 
    

    private static final List<Card> deck = new ArrayList<>();

	// Initialize the static deck
    static 
    {		
        for (Suit suit : Suit.values())
        {
            for (Rank rank : Rank.values())
            {
                deck.add(new Card(rank, suit));
            }
        }
    }

    public static ArrayList<Card> newDeck() 
    {	
        // Return copy of prototype deck 
        ArrayList<Card> newDeck = new ArrayList<>(deck);
        Collections.shuffle(newDeck);
        return newDeck;
    }
    
    public Rank getRank()
    {
        return rank;
    }
    public Suit getSuit()
    {
        return suit;
    }
    
    
}
