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
    private Hand BidCards;
  
    
    private void InIBid()
    {
        TotalBid = 0.0;
        BidCards = new Hand();
    }
    
    public Bid(Card FirstCard,Card SecondCard,Double BetValue)
    {
        InIBid();
        TotalBid = BetValue;
        BidCards.AddCard(FirstCard);
        BidCards.AddCard(SecondCard);
    }
    
    
    public Bid(ArrayList<Card> Cards,Double TotalBid)
    {
        InIBid();
        this.TotalBid = TotalBid;
        for (Card card : Cards) 
        {
            BidCards.AddCard(card);  
        }
    }
    
    
    public Bid(Card FirstCard,Double BidWanted)
    {
        InIBid();
        TotalBid = BidWanted;
        BidCards.AddCard(FirstCard);        
    }
    
      
    public Double getTotalBid() {
        return TotalBid;
    }

    public int GetSumOfCards()
    {
        return this.BidCards.getSumCards();
    }
    
    public void DoubleBid(Card card)
    {
        TotalBid = TotalBid *2;
        HitBid(card);
    }
    
    public void HitBid(Card card)
    {
        BidCards.AddCard(card);
    }
    
    public int GetNumberOfCards()
    {
        return BidCards.GetNumberOfCards();
    }
    
    public ArrayList<Card> getCards() {
        return BidCards.getCards();
    }
    
}
