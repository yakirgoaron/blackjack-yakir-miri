/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.Bid;
import EngineLogic.Card;
import EngineLogic.CompPlayer;
import EngineLogic.Hand;
import EngineLogic.HumanPlayer;
import EngineLogic.Player;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author yakir
 */
public class ParticipantContainer
{
    private Pane ParticipantImage;
    private Queue<Pane> Hands;
    private Queue<Pane> OrderHand;
    private HashMap<Hand,Pane> HandView;
    private Label Bid1Value;
    private Label Bid2Value;
    public ParticipantContainer(VBox Hand1,VBox Hand2,Pane ParticipantImage,Label PlaceBid1,Label PlaceBid2)
    {
        this.ParticipantImage = ParticipantImage;
        Hands = new LinkedList<>();
        OrderHand = new LinkedList<>();
        Hand1.setSpacing(-45.0);
        Hand2.setSpacing(-45.0);
        OrderHand.add(Hand1);
        OrderHand.add(Hand2);
        Hands.addAll(OrderHand);
        HandView = new HashMap<>();
        Bid1Value = PlaceBid1;
        Bid1Value.getStyleClass().add("BidClass");
        Bid2Value = PlaceBid2;
    }
    public ParticipantContainer(HBox Hand, Pane PlayerImage){
        this.ParticipantImage = PlayerImage;
        Hands = new LinkedList<>();
        Hand.setSpacing(-45.0);
        OrderHand = new LinkedList<>();
        OrderHand.add(Hand);
        Hands.add(Hand); 
        HandView = new HashMap<>();
    }
    public void PrintPlayerInfo(Player plToPrint)
    {
        PlayerView pl = new PlayerView(plToPrint.getName(), plToPrint instanceof HumanPlayer);
        ParticipantImage.getChildren().add(pl);
        
        ParticipantImage.setEffect(new DropShadow(25.0, Color.RED));
        //ParticipantImage.getStyleClass().add("PlayerFocus");
    }
    
    public void PrintHandInfo(Hand currHand)
    {
        if(!HandView.containsKey(currHand))
        {
            Pane Player = Hands.remove();
            HandView.put(currHand, Player);
            
        }
        addcards(currHand);
        if (currHand instanceof Bid)
        {
            Bid1Value.setText(new DecimalFormat("####.##").format(((Bid)currHand).getTotalBid()));
        }
    }
    
    public void GlowHandInfo(Hand currHand)
    {
        HandView.get(currHand).setEffect(new DropShadow(25.0, Color.RED));
    }
    
    public void ClearGlowHandInfo(Hand currHand)
    {
        HandView.get(currHand).setEffect(null);
    }
    
    private void addcards(Hand currHand)
    {
         Pane curr = HandView.get(currHand);
         curr.getChildren().clear();
         for (Card curCard : currHand.getCards()) 
         {
             CardView cd = new CardView(curCard);
             curr.getChildren().add(cd);
         }
    }

    public void ClearCards() {
        for (Entry<Hand,Pane> entry: HandView.entrySet()) {       
            entry.getValue().getChildren().clear();            
        }
        Hands.clear();
        Hands.addAll(OrderHand);
        HandView.clear();
    }

    public void ClearEffects() {
        if (ParticipantImage.getStyleClass().contains("PlayerFocus"))
            ParticipantImage.getStyleClass().remove("PlayerFocus");
        for (Entry<Hand, Pane> entry : HandView.entrySet()) {
            
            entry.getValue().setEffect(null);
            
        }
       ParticipantImage.setEffect(null);
    }

    public void removePlayer() {
        this.ParticipantImage.getChildren().clear();
        this.Bid1Value.setText("");
        this.Bid2Value.setText("");
        ClearCards();
    }
}
