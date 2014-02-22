/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GameEngine;

import java.util.List;

/**
 *
 * @author yakir
 */
public abstract class Player {
    String Name;
    Double Money;
    List<Bid> Bids;
    Double TotalBid;
    final Double StartMoney = 1000.0;
    
    
    public Player()
    {
        Money = StartMoney;
        TotalBid = 0.0;
    }
    
    public String getName() {
        return Name;
    }

    public Double getMoney() {
        return Money;
    }

    public List<Bid> getBids() {
        return Bids;
    }

    public Double getTotalBid() {
        return TotalBid;
    }
    public void GivePlayerCards(Card FirstCard,Card SecondCard)
    {
    
    }
    void DoubleBid(Bid bid)
    {
    }
    void HitBid(Bid bid,Card card)
    {
    }
    void Split()
    {
    }
    
    
    
    
    
}
