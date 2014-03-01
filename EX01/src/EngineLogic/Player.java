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
 * @author yakir
 */
public abstract class Player {
    String Name;
    Double Money;
    ArrayList<Bid> Bids;
    Double TotalBid;
    final Double StartMoney = 1000.0;
    
    
    public Player()
    {
        Money = StartMoney;
        TotalBid = 0.0;
        Bids = new ArrayList<>();
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
    public void GivePlayerCards(Card FirstCard,Card SecondCard,Double BetValue)
    {         
         Bids.add(new Bid(FirstCard,SecondCard,BetValue));    
    }
    void DoubleBid(Bid bid)
    {
    }
    void HitBid(Bid bid,Card card)
    {
    }
    
    
    private Boolean CheckForSplit()
    {
       if(Bids.size() != 1)
           return false;
       if(Bids.get(0).getCards().size() != 2)
           return false;
        if(Bids.get(0).getTotalBid()*2 > Money)
           return false;
       Card FirstCard = Bids.get(0).getCards().get(0);
       Card SecondCard = Bids.get(0).getCards().get(1);
       if(FirstCard.getRank().equals(SecondCard.getRank()))
           return false;
      
       return true;
    }
    
    void Split()
    {
        Bid BidToSplit = Bids.get(0);
        Card FirstCard = BidToSplit.getCards().get(0);
        Card SecondCard = BidToSplit.getCards().get(1);
        BidToSplit.getCards().remove(1);
        Bid NewBid = new Bid(SecondCard,BidToSplit.getTotalBid());
        Bids.add(NewBid);  
    }
    
    
    
    
    
}
