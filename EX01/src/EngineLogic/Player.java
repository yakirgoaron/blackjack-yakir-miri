/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Exception.RulesDosentAllowException;
import EngineLogic.Exception.TooLowMoneyException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yakir
 */
public abstract class Player {
    protected String Name;
    protected Double Money;
    protected ArrayList<Bid> Bids;
    protected Double TotalBid;
    protected final Double StartMoney = 1000.0;
    
    
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
    
    
    public void DoubleBid(Bid bid,Card card) throws TooLowMoneyException
    {
        if(bid.getTotalBid() *2 > Money)
            throw new TooLowMoneyException();
        bid.DoubleBid(card);
    }
    
    public void HitBid(Bid bid,Card card)
    {
        bid.HitBid(card);
    }
    
    
    private void CheckForSplit() throws RulesDosentAllowException, TooLowMoneyException
    {
       if(Bids.size() != 1)
           throw new RulesDosentAllowException("Split only allowed once");
       if(Bids.get(0).getCards().size() != 2)
           throw new RulesDosentAllowException("Too many cards");
        if(Bids.get(0).getTotalBid()*2 > Money)
           throw new TooLowMoneyException();
       Card FirstCard = Bids.get(0).getCards().get(0);
       Card SecondCard = Bids.get(0).getCards().get(1);      
       if(FirstCard.getRank().compareTo(SecondCard.getRank()) != 0)
           throw new RulesDosentAllowException("Cards are not the same");
    }
    
    public void Split() throws RulesDosentAllowException, TooLowMoneyException
    {
        CheckForSplit();
        Bid BidToSplit = Bids.get(0);
        Card SecondCard = BidToSplit.getCards().get(1);
        BidToSplit.getCards().remove(1);
        Bid NewBid = new Bid(SecondCard,BidToSplit.getTotalBid());
        Bids.add(NewBid);  
    }
    
    
    
    
    
}
