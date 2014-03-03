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
   private List<Card> Cards; 
   private int SumofCards;
   
   public Dealer()
   {
       SumofCards = 0;
       Cards = new ArrayList<>();
   }
   
   public void GiveCard(Card card)
   {
        Cards.add(card);
   }
   
}
