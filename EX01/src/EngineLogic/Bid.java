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
        AddCardToBid(FirstCard);
        AddCardToBid(SecondCard);
    }
    
    
    public Bid(ArrayList<Card> Cards,Double TotalBid)
    {
        this.TotalBid = TotalBid;
        for (Card card : Cards) 
        {
            AddCardToBid(card);  
        }
    }
    
    
    public Bid(Card FirstCard,Double BidWanted)
    {
        SumCards=0;
        TotalBid = BidWanted;
        Cards = new ArrayList<>();
        AddCardToBid(FirstCard);        
    }
    
    private void AddCardToBid(Card FirstCard)
    {
        AddCardsToList(FirstCard);
        CheckAceCard(FirstCard);
    }
    
    public List<Card> getCards() {
        return Cards;
    }
    
    public Double getTotalBid() {
        return TotalBid;
    }

    public int getSumCards() 
    {
        if(IsHaveAce && SumCards < 10)
            SumCards += 10;
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
