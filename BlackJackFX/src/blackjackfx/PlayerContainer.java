/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackfx;

import EngineLogic.Bid;
import EngineLogic.Hand;
import EngineLogic.Player;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author miri
 */
public class PlayerContainer extends ParticipantContainer{
  
    private Label Bid1Value;
    private Label Bid2Value;
    private Label MoneyValue;
    private Queue<Label> Hands;
    private Queue<Label> OrderHand;
    private HashMap<Bid,Label> BidView;
    
    public PlayerContainer(VBox Hand1,VBox Hand2,Pane ParticipantImage,
                           Label PlaceBid1,Label PlaceBid2, Label Money, 
                           Label Message, Pane CradsDeck)
    {
        super(Hand1,Hand2,ParticipantImage, Message, CradsDeck);
        Bid1Value = PlaceBid1;
        Bid2Value = PlaceBid2;
        MoneyValue = Money;
        Bid1Value.getStyleClass().add("BidClass");
        Bid2Value.getStyleClass().add("BidClass");
        BidView = new HashMap<>();
        OrderHand = new LinkedList<>();
        Hands = new LinkedList<>();
        OrderHand.add(PlaceBid1);
        OrderHand.add(PlaceBid2);
        Hands.addAll(OrderHand);
    }
    
    public void PrintBidInfo(Bid currBid)
    {
        PrintHandInfo(currBid);
        if(!BidView.containsKey(currBid))
        {
            BidView.put(currBid, Hands.remove());
            
        }
        BidView.get(currBid).setText(new DecimalFormat("####.##").format(((Bid)currBid).getTotalBid()));
    }
    
    @Override
    public void RemovePlayer(){
        super.RemovePlayer();
        this.Bid1Value.setText("");
        this.Bid2Value.setText("");
        Hands.clear();
        Hands.addAll(OrderHand);
    }
    
    @Override
    public void PrintPlayerInfo(Player plToPrint){
        MoneyValue.setText(plToPrint.getMoney().toString());
        super.PrintPlayerInfo(plToPrint);
    }
    
    @Override
    public void ClearCards()
    {
        super.ClearCards();
        this.Bid1Value.setText("");
        this.Bid2Value.setText("");
        Hands.clear();
        Hands.addAll(OrderHand);
    }
}
