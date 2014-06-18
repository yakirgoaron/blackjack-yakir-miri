/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EngineLogic;

import EngineLogic.Exception.PlayerResigned;
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
    
    @Override
    public Double GetBidForPlayer(Communicable commGetBid) throws PlayerResigned
    {
        boolean ValidBid = false;
        
        Double Playerbid = commGetBid.GetBidForPlayer(this);
        while (!ValidBid)
        {
            if (Playerbid < 0){
                Playerbid = commGetBid.GetBidForPlayer(this);
            }
            else if (Playerbid > Money){   
                Playerbid = commGetBid.GetBidForPlayer(this);
            }
            else
                ValidBid = true;
        }
        return Playerbid;
    }
}
