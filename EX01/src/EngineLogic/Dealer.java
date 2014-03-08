/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Communicable.PlayerAction;
import java.util.ArrayList;

/**
 *
 * @author yakir
 */
public class Dealer 
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
    
    public PlayerAction Play(){
        
        if (getSumofCards() < 17)
            return PlayerAction.HIT;       
        return PlayerAction.STAY;       
    }
   
}
