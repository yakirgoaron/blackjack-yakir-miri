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
    public enum Rank {
        DEUCE {
            @Override
            int Value(){return 2;}},
        THREE {
            @Override
            int Value(){return 3;}},
         FOUR  {
             @Override
             int Value(){return 4;}},
         FIVE  {
             @Override
             int Value(){return 5;}},
         SIX   {
             @Override
             int Value(){return 6;}},
         SEVEN {
             @Override
             int Value(){return 7;}},
         EIGHT {
             @Override
             int Value(){return 8;}},
         NINE  {
             @Override
             int Value(){return 9;}},
         TEN   {
             @Override
             int Value(){return 10;}},
         JACK  {
             @Override
             int Value(){return 10;}},
         QUEEN {
             @Override
             int Value(){return 10;}},
         KING  {
             @Override
             int Value(){return 10;}},
         ACE  {
             @Override
             int Value(){return 1;}};
         abstract int Value();
    }
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
    /*static 
    {		
        for (Suit suit : Suit.values())
        {
            for (Rank rank : Rank.values())
            {
                deck.add(new Card(rank, suit));
            }
        }
    }*/

    public static ArrayList<Card> newDeck() 
    {	
        ArrayList<Card> newDeck = new ArrayList<>();
        // Return copy of prototype deck 
        for (Suit suit : Suit.values())
        {
            for (Rank rank : Rank.values())
            {
                newDeck.add(new Card(rank, suit));
            }
        }
        
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
