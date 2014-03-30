/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.Bid;
import EngineLogic.Card;
import EngineLogic.CompPlayer;
import EngineLogic.HumanPlayer;
import EngineLogic.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author yakir
 */
public class PlayerContainer
{
    private Pane PlayerImage;
    private Queue<VBox> Bids;
    private HashMap<Bid,VBox> BidView;
    
    public PlayerContainer(VBox Bid1,VBox Bid2,Pane PlayerImage)
    {
        this.PlayerImage = PlayerImage;
        Bids = new LinkedList<>();
        Bid1.setSpacing(-45.0);
        Bid2.setSpacing(-45.0);
        Bids.add(Bid1);
        Bids.add(Bid2);
        BidView = new HashMap<>();
    }
    public void PrintPlayerInfo(Player plToPrint)
    {
        PlayerView pl = new PlayerView(plToPrint.getName(), plToPrint instanceof HumanPlayer);
        PlayerImage.getChildren().add(pl);
    }
    
    public void PrintBidInfo(Bid currBid)
    {
        if(!BidView.containsKey(currBid))
        {
            BidView.put(currBid,Bids.remove());
        }
        addcards(currBid);
    }
    private void addcards(Bid currBid)
    {
         VBox curr = BidView.get(currBid);
         curr.getChildren().clear();
         for (Card curCard : currBid.getCards()) 
         {
             CardView cd = new CardView(curCard);
             curr.getChildren().add(cd);
         }
         
    }
}
