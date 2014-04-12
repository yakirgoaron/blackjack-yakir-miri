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
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
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
import javafx.util.Duration;

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
    private Pane pDeckPane;
    
    public ParticipantContainer(VBox Hand1,VBox Hand2,Pane ParticipantImage,Pane CardsDeck)
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
        pDeckPane = CardsDeck;
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
    }
    
    public void GlowHandInfo(Hand currHand)
    {
        HandView.get(currHand).setEffect(new DropShadow(25.0, Color.RED));
    }
    
    public void ClearGlowHandInfo(Hand currHand)
    {
        if (HandView.get(currHand) != null)
            HandView.get(currHand).setEffect(null);
    }
    
    private void addcards(Hand currHand)
    {
         Pane curr = HandView.get(currHand);
         curr.getChildren().clear();
         for (Card curCard : currHand.getCards()) 
         {
             CardView cd = new CardView(curCard);
             cd.setVisible(false);
             Pane Temp = DuplicatePane(pDeckPane);
             pDeckPane.getChildren().add(Temp);
             Temp.getChildren().add(new CardView(curCard));
             PullCardUI(curr,cd,pDeckPane);
             curr.getChildren().add(cd);
         }
    }
    
    private Pane DuplicatePane(Pane Source)
    {
        Pane Temp = new Pane();
        Temp.setVisible(true);
        Temp.setLayoutX(0);
        Temp.setLayoutY(0);
        Temp.setMaxHeight(Source.getMaxHeight());
        Temp.setMaxWidth(Source.getMaxWidth());
        Temp.setMinHeight(Source.getMinHeight());
        Temp.setMinWidth(Source.getMinWidth());
        
        return Temp;
    }
    
    private void PullCardUI(final Pane AddCardFinish,final CardView CardToAdd,final Pane pToMove)
    {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2.0), pToMove);
        final double x = pToMove.getLayoutX();
        final double y = pToMove.getLayoutY();
        translateTransition.setFromX(0);
        translateTransition.setFromY(0);
        //translateTransition.setToX(-200);
        //translateTransition.setToY(200);
        translateTransition.setToX(ParticipantImage.getLayoutX() - x);
        translateTransition.setToY(ParticipantImage.getLayoutY() + y + pToMove.getMaxHeight()+200);
        /*translateTransition.setCycleCount(4);
        translateTransition.setAutoReverse(true);*/
        
         translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                pToMove.setLayoutX(x);
                pToMove.setLayoutY(y);
                pToMove.setVisible(false);
                CardToAdd.setVisible(true);
            }
        });
        translateTransition.play();
        
        
        
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

    public void RemovePlayer() {
        this.ParticipantImage.getChildren().clear();
        ClearCards();
    }
}
