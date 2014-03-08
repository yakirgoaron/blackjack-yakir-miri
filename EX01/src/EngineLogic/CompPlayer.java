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
public class CompPlayer extends Player
{
    static int CompIdGen=1;
    public CompPlayer()
    {
        Name = "Comp"+CompIdGen;
        CompIdGen++;
    }
    
    public CompPlayer(String Name,Double Money,ArrayList<Bid> Bids)
    {
        this.Name = Name;
        this.Money = Money;
        this.Bids = Bids;
    }
    
    public void Play()
    {
        
    }
}
