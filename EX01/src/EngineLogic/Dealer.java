/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Communicable.PlayerAction;
import EngineLogic.Exception.RulesDosentAllowException;
import EngineLogic.Exception.TooLowMoneyException;
import java.util.ArrayList;

/**
 *
 * @author yakir
 */
public class Dealer implements AIPlayer
{
   private Hand DealerCards; 
   
   public Dealer()
   {
       DealerCards = new Hand();
   }
   
   public Dealer(ArrayList<Card> Cards)
   {
       this.DealerCards = new Hand();   
       
       for (Card card : Cards) 
        {
            DealerCards.AddCard(card);  
        }
   }

    public Hand getDealerCards() {
        return DealerCards;
    }
   
   public void GiveCard(Card card)
   {
        DealerCards.AddCard(card);
   }

    public int getSumofCards() {
        return DealerCards.getSumCards();
    }
    
    public void HitBid(Card card)
    {
        DealerCards.AddCard(card);
    }
   
    public ArrayList<Card> getCards() {
        return DealerCards.getCards();
    }
    
   @Override
    public PlayerAction Play(Hand hand){
        
        if (hand.getSumCards() < 17)
            return PlayerAction.HIT;       
        return PlayerAction.STAY;       
    }

    @Override
    public void Split() throws RulesDosentAllowException, TooLowMoneyException 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void HitBid(Hand bid, Card card) 
    {
        bid.AddCard(card);
    }

    @Override
    public void DoubleBid(Bid bid, Card card) throws TooLowMoneyException 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
}
