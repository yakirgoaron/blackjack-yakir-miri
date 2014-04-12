/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackfx;

import EngineLogic.Bid;
import EngineLogic.Hand;
import java.text.DecimalFormat;
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
    
    public PlayerContainer(VBox Hand1,VBox Hand2,Pane ParticipantImage,Label PlaceBid1,Label PlaceBid2)
    {
        super(Hand1,Hand2,ParticipantImage);
        Bid1Value = PlaceBid1;
        Bid2Value = PlaceBid2;
        Bid1Value.getStyleClass().add("BidClass");
    }
    
    public void PrintBidInfo(Bid currBid)
    {
        PrintHandInfo(currBid);
        Bid1Value.setText(new DecimalFormat("####.##").format(((Bid)currBid).getTotalBid()));
    }
    
    @Override
    public void RemovePlayer(){
        super.RemovePlayer();
        this.Bid1Value.setText("");
        this.Bid2Value.setText("");
    }
    
}
