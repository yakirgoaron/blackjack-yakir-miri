/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.ServerClasses;

/**
 *
 * @author yakir
 */
public class Card 
{
    public enum Rank {
        TWO {
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
            
    public Rank getRank()
    {
        return rank;
    }
    public Suit getSuit()
    {
        return suit;
    }
    
    public static Card ConvertToLocal(game.client.ws.Card ServerCard)
    {
        return new Card(Rank.valueOf(ServerCard.getRank().value()),
                        Suit.valueOf(ServerCard.getSuit().value()));
    }
    
    
}
