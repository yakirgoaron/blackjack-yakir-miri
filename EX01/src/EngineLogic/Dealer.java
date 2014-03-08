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
public class Dealer 
{
   private Hand DealerCards; 
   
   public Dealer()
   {
       DealerCards = new Hand();
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
   
}
