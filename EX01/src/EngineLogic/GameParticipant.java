/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Exception.RulesDosentAllowException;
import EngineLogic.Exception.TooLowMoneyException;

/**
 *
 * @author yakir
 */
public interface GameParticipant {
     public void Split()throws RulesDosentAllowException, TooLowMoneyException;
     public void HitBid(Hand bid,Card card);
     public void DoubleBid(Bid bid,Card card)throws TooLowMoneyException;
}
