/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yakir & miri
 */
public class Bid 
{
    private Double TotalBid;
    private ArrayList<Card> Cards;
    private int SumCards;
    private Boolean IsHaveAce;
    
    
    public Bid(Card FirstCard,Card SecondCard,Double BetValue)
    {
        SumCards=0;
        TotalBid = BetValue;
        IsHaveAce = false;
        Cards = new ArrayList<>();
        AddCardsToList(FirstCard);
        AddCardsToList(SecondCard);
        CheckAceCard(FirstCard);
        CheckAceCard(SecondCard);
    }
    
    
    public Bid(ArrayList<Card> Cards,Double TotalBid)
    {
        this.Cards = Cards;
        this.TotalBid = TotalBid;
    }
    
    
    public Bid(Card FirstCard,Double BidWanted)
    {
        SumCards=0;
        TotalBid = 50.0;
        Cards = new ArrayList<>();
        Cards.add(FirstCard);
        
    }
    
    public List<Card> getCards() {
        return Cards;
    }
    public Double getTotalBid() {
        return TotalBid;
    }

    public int getSumCards() {
        return SumCards;
    }
    
    public void DoubleBid(Card card)
    {
        TotalBid = TotalBid *2;
        HitBid(card);
    }
    public void HitBid(Card card)
    {
        Cards.add(card);
    }
    
    private void CheckAceCard(Card First)
    {
        IsHaveAce = !IsHaveAce && 
                     First.getRank().compareTo(Card.Rank.ACE) == 0;
    }
    private void AddCardsToList(Card CardToAdd)
    {
        Cards.add(CardToAdd);
        SumCards += CardToAdd.getRank().Value();
    }
}
