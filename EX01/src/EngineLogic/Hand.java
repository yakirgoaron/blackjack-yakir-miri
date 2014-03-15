/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import java.util.ArrayList;

/**
 *
 * @author yakir
 */
public class Hand 
{
    private ArrayList<Card> Cards;   
    private Boolean IsHaveAce;
    private int SumCards;
    public Hand()
    {
        SumCards=0;
        Cards = new ArrayList<>();
        IsHaveAce = false;
    }
    
    private void AddCardToHand(Card FirstCard)
    {
        AddCardsToList(FirstCard);
        CheckAceCard(FirstCard);
    }
   
    private void CheckAceCard(Card First)
    {
        IsHaveAce = IsHaveAce || 
                    (First.getRank().compareTo(Card.Rank.ACE) == 0);
    }
    
    private void AddCardsToList(Card CardToAdd)
    {
        Cards.add(CardToAdd);
        SumCards += CardToAdd.getRank().Value();
    }
     
    public int getSumCards() 
    {
        if(IsHaveAce && SumCards <= 11)
            return SumCards + 10;
        return SumCards;
    }
    
    public void AddCard(Card card)
    {
        AddCardToHand(card);
    }
    
    public int GetNumberOfCards()
    {
        return this.Cards.size();
    }
    
    public ArrayList<Card> getCards() {
        return Cards;
    }
}
