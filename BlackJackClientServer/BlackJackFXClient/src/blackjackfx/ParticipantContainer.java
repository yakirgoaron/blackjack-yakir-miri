/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import blackjackfx.ServerClasses.Card;
import blackjackfx.ServerClasses.PlayerInfo;
import blackjackfx.ServerClasses.PlayerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
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
    private HashMap<String,Pane> HandView;
    private Pane pDeckPane;
    private Label lblMessage;
    private boolean IsDealer;
    
    public ParticipantContainer(VBox Hand1,VBox Hand2,Pane ParticipantImage,
                                Label Message, Pane CardsDeck)
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
        lblMessage = Message;
        lblMessage.getStyleClass().add("PlayerMessage");
        pDeckPane = CardsDeck;
        IsDealer = false;
    }
    
    public ParticipantContainer(HBox Hand, Pane PlayerImage, Label Message, Pane CardsDeck){
        this.ParticipantImage = PlayerImage;
        Hands = new LinkedList<>();
        Hand.setSpacing(-45.0);
        OrderHand = new LinkedList<>();
        OrderHand.add(Hand);
        Hands.add(Hand); 
        HandView = new HashMap<>();
        lblMessage = Message;
        lblMessage.getStyleClass().add("PlayerMessage");
        pDeckPane = CardsDeck;
        IsDealer = true;
    }
    public void PrintPlayerInfo(PlayerInfo plToPrint)
    {
        PlayerView pl = new PlayerView(plToPrint.getName(), plToPrint.getType().equals(PlayerType.HUMAN));
        ParticipantImage.getChildren().add(pl);
        SetParticipantEffect();
    }
    
    public void SetParticipantEffect()
    {
        Bloom bl = new Bloom(0.00001);
        bl.setInput(new DropShadow(25.0, Color.RED));
        ParticipantImage.setEffect( bl);  
    }
    
    public void PrintHandInfo(String HandName,List<Card> currHand)
    {
        if(!HandView.containsKey(HandName))
        {
            Pane Player = Hands.remove();
            HandView.put(HandName, Player);           
        }
        addcards(HandName,currHand);
    }
    
    public void GlowHandInfo(String HandName)
    {
        HandView.get(HandName).setEffect(new DropShadow(35.0, Color.YELLOW));
    }
    
    public void ClearGlowHandInfo(String HandName)
    {
        if (HandView.get(HandName) != null)
            HandView.get(HandName).setEffect(null);
    }
    
    private void addcards(String HandName,List<Card> currHand)
    {      
        Pane curr = HandView.get(HandName);        
         
        /*
        if (curr.getChildren().size() > currHand.size())
        {
           curr.getChildren().clear(); 
        }*/
        
        curr.getChildren().clear(); 
        
        for (Card curCard : currHand) 
        {
            CardView cd = new CardView(curCard);
            if(!curr.getChildren().contains(cd))
            {
                cd.setVisible(false);
                pDeckPane.getChildren().add(new CardView(curCard));
                PullCardUI(cd,pDeckPane);
                curr.getChildren().add(cd);
             }
         }
    }
        
    private void PullCardUI(final CardView CardToAdd,final Pane pToMove)
    {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2.0), pToMove);
        final double x = pToMove.getLayoutX();
        final double y = pToMove.getLayoutY();
        translateTransition.setFromX(0);
        translateTransition.setFromY(0);
        if(IsDealer)
        {
            translateTransition.setToX(-ParticipantImage.getLayoutX() + 200);
            translateTransition.setToY(ParticipantImage.getLayoutY());
        }
        else
        {
            translateTransition.setToX(ParticipantImage.getParent().getLayoutX() - x);
            translateTransition.setToY(ParticipantImage.getParent().getLayoutY() + y + pToMove.getMaxHeight()+200);
        }
        
         translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                pToMove.setLayoutX(x);
                pToMove.setLayoutY(y);
                CardToAdd.setVisible(true);
                pToMove.getChildren().clear();
            }
        });
        translateTransition.play();          
    }
    
    public void ClearCards() {
        for (Entry<String,Pane> entry: HandView.entrySet()) {       
            entry.getValue().getChildren().clear();            
        }
        Hands.clear();
        Hands.addAll(OrderHand);
        HandView.clear();
        
        lblMessage.setText("");
    }

    public void ClearEffects() {
    
        for (Entry<String, Pane> entry : HandView.entrySet()) {         
            entry.getValue().setEffect(null);         
        }
        ParticipantImage.setEffect(null);
    }

    public void RemovePlayer() {
        this.ParticipantImage.getChildren().clear();
        ClearCards();
    }

    public void PrintMessage(String Message) {
        lblMessage.setText(Message);
    }
}
