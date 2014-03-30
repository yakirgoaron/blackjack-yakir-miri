/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.Bid;
import EngineLogic.CompPlayer;
import EngineLogic.Player;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
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
        Bids.add(Bid1);
        Bids.add(Bid2);
    }
    public void PrintPlayerInfo(Player plToPrint)
    {
        
    }
    
}
