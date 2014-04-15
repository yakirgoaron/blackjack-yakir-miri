/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import java.util.ArrayList;

/**
 *
 * @author yakir & miri
 */
public class Bid extends Hand
{
    private Double TotalBid;
  
    
    private void InIBid()
    {
        TotalBid = 0.0;
       
    }
    
    public Bid(Card FirstCard,Card SecondCard,Double BetValue)
    {
        InIBid();
        TotalBid = BetValue;
        AddCard(FirstCard);
        AddCard(SecondCard);
    }
    
    
    public Bid(ArrayList<Card> Cards,Double TotalBid)
    {
        InIBid();
        this.TotalBid = TotalBid;
        for (Card card : Cards) 
        {
            AddCard(card);  
        }
    }
    
    
    public Bid(Card FirstCard,Double BidWanted)
    {
        InIBid();
        TotalBid = BidWanted;
        AddCard(FirstCard);        
    }
    
      
    public Double getTotalBid() {
        return TotalBid;
    }
    
    public void DoubleBid(Card card)
    {
        TotalBid = TotalBid *2;
        HitBid(card);
    }
    
    public void HitBid(Card card)
    {
        AddCard(card);
    }
        
}
