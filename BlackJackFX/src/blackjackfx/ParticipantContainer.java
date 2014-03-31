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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author yakir
 */
public class ParticipantContainer
{
    private Pane ParticipantImage;
    private Queue<VBox> Hands;
    private HashMap<Hand,VBox> HandView;
    
    public ParticipantContainer(VBox Hand1,VBox Hand2,Pane ParticipantImage)
    {
        this.ParticipantImage = ParticipantImage;
        Hands = new LinkedList<>();
        Hand1.setSpacing(-45.0);
        Hand2.setSpacing(-45.0);
        Hands.add(Hand1);
        Hands.add(Hand2);
        HandView = new HashMap<>();
    }
    public ParticipantContainer(VBox Hand, Pane PlayerImage){
        this.ParticipantImage = PlayerImage;
        Hands = new LinkedList<>();
        Hand.setSpacing(-45.0);
        Hands.add(Hand); 
        HandView = new HashMap<>();
    }
    public void PrintPlayerInfo(Player plToPrint)
    {
        PlayerView pl = new PlayerView(plToPrint.getName(), plToPrint instanceof HumanPlayer);
        ParticipantImage.getChildren().add(pl);
    }
    
    public void PrintHandInfo(Hand currHand)
    {
        if(!HandView.containsKey(currHand))
        {
            HandView.put(currHand,Hands.remove());
        }
        addcards(currHand);
    }
    private void addcards(Hand currHand)
    {
         VBox curr = HandView.get(currHand);
         curr.getChildren().clear();
         for (Card curCard : currHand.getCards()) 
         {
             CardView cd = new CardView(curCard);
             curr.getChildren().add(cd);
         }
         
    }
}
