/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameEngine;

import java.util.List;

/**
 *
 * @author yakir & miri
 */
public class Bid 
{
    Double TotalBid;
    List<Card> Cards;
    int SumCards;

    public Bid(Card FirstCard,Card SecondCard)
    {
        SumCards=0;
        TotalBid = 50.0;
        Cards.add(FirstCard);
        Cards.add(SecondCard);
    }
    
    public Double getTotalBid() {
        return TotalBid;
    }

    public int getSumCards() {
        return SumCards;
    }
    
    public void DoubleBid()
    {
    }
    public void HitBid(Card card)
    {
    }
    
    
    
}
