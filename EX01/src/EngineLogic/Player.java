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
public abstract class Player implements GameParticipant{
    protected String Name;
    protected Double Money;
    protected ArrayList<Bid> Bids;
    protected Double TotalBid;
    protected final Double StartMoney = 1000.0;    
    private final static int INIT_NUM_CARDS = 2;
    private final static Double WIN_BJ_ONSTART = 2.5;
    private final static Double WIN_BJ = 2.0;
    
    
    public Player()
    {
        Money = StartMoney;
        TotalBid = 0.0;
        Bids = new ArrayList<>();
    }
    
    @Override
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
    
    @Override
    public void DoubleBid(Bid bid,Card card) throws TooLowMoneyException
    {
        if(bid.getTotalBid() *2 > Money)
            throw new TooLowMoneyException();
        bid.DoubleBid(card);
    }
    
    @Override
    public void HitBid(Hand bid,Card card)
    {
        bid.AddCard(card);
    }
    
    
    private void CheckForSplit() throws RulesDosentAllowException, TooLowMoneyException
    {
       if(Bids.size() != 1)
           throw new RulesDosentAllowException("Split only allowed once");
       if(Bids.get(0).GetNumberOfCards() != 2)
           throw new RulesDosentAllowException("Too many cards");
        if(Bids.get(0).getTotalBid()*2 > Money)
           throw new TooLowMoneyException();
       Card FirstCard = Bids.get(0).getCards().get(0);
       Card SecondCard = Bids.get(0).getCards().get(1);      
       if(FirstCard.getRank().compareTo(SecondCard.getRank()) != 0)
           throw new RulesDosentAllowException("Cards are not the same");
    }
    
    @Override
    public void Split() throws RulesDosentAllowException, TooLowMoneyException
    {
        CheckForSplit();
        Bid BidToSplit = Bids.get(0);
        Card SecondCard = BidToSplit.getCards().get(1);
        BidToSplit.getCards().remove(1);
        Bid NewBid = new Bid(SecondCard,BidToSplit.getTotalBid());
        Bids.add(NewBid);  
    }
    
    abstract public Double GetBidForPlayer(Communicable commGetBid); 
       
    public void HandleEndOfRound(int DealerSumOfCards){
        
        for (Bid bid: getBids()){
            HandleBidEndRound(bid, DealerSumOfCards);
        }     
        
        Bids.clear();
    }
    
    private void HandleBidEndRound(Bid bid, int DealerSumOfCards) {
        if (!CheckBJ(bid))
            HandleLose(bid, DealerSumOfCards);
    }
        
    private boolean CheckBJ(Bid bid) {
        
        Double BetToAdd;
        
        if (bid.getSumCards() == GameEngine.BLACKJACK){
            if (bid.getCards().size() == INIT_NUM_CARDS)
               BetToAdd =  bid.getTotalBid() * WIN_BJ_ONSTART;
            else
               BetToAdd = bid.getTotalBid() * WIN_BJ;
            Money += BetToAdd;
            return true;
        }
        return false;
    }

    private void HandleLose(Bid bid, int DealerSumOfCards) {
        if (((DealerSumOfCards > bid.getSumCards()) && 
             (DealerSumOfCards <= GameEngine.BLACKJACK)) ||
            ((bid.getSumCards() > GameEngine.BLACKJACK) && 
             (DealerSumOfCards <= GameEngine.BLACKJACK)))
           
            Money -= bid.getTotalBid();        
    }
}
