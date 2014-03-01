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
    public enum Rank {DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
                          TEN, JACK, QUEEN, KING, ACE}
    public enum Suit {CLUBS, DIAMONDS, HEARTS, SPADES}
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

    
}
