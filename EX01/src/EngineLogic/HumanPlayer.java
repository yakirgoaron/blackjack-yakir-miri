/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import java.util.ArrayList;

/**
 *
 * @author yakir
 */
public class HumanPlayer extends Player
{
      
    public HumanPlayer(String Name)
    {
        this.Name = Name;
    }
    
    public HumanPlayer(String Name,Double Money,ArrayList<Bid> Bids)
    {
        this.Name = Name;
        this.Money = Money;
        this.Bids = Bids;
    }
}
