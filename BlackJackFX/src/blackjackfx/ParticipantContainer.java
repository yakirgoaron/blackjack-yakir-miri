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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
    private Label lblMessage;
    
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
    }
    public void PrintPlayerInfo(Player plToPrint)
    {
        PlayerView pl = new PlayerView(plToPrint.getName(), plToPrint instanceof HumanPlayer);
        ParticipantImage.getChildren().add(pl);
        Bloom bl = new Bloom(0.00001);
        bl.setInput(new DropShadow(25.0, Color.RED));
        //ParticipantImage.setEffect(new DropShadow(25.0, Color.RED));
        ParticipantImage.setEffect( bl);
        
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
        Bloom bl = new Bloom(0.9);
        bl.setInput(new DropShadow(25.0, Color.RED));
        //HandView.get(currHand).setEffect(new DropShadow(25.0, Color.RED));
        HandView.get(currHand).setEffect(bl);
    }
    
    public void ClearGlowHandInfo(Hand currHand)
    {
        if (HandView.get(currHand) != null)
            HandView.get(currHand).setEffect(null);
    }
    
    private void addcards(Hand currHand)
    {
        
        Pane curr = HandView.get(currHand);        
        ArrayList<Card> HandCards = currHand.getCards();
         
        if (curr.getChildren().size() > HandCards.size())
        {
           curr.getChildren().clear(); 
        }
        
        for (Card curCard : HandCards) 
        {
            CardView cd = new CardView(curCard);
            if(!curr.getChildren().contains(cd))
            {
                cd.setVisible(false);
                //Pane Temp = DuplicatePane(pDeckPane);
                pDeckPane.getChildren().add(new CardView(curCard));
                //Temp.getChildren().add(new CardView(curCard));
                PullCardUI(curr,cd,pDeckPane);
                curr.getChildren().add(cd);
             }
         }
    }
        
    private void PullCardUI(final Pane AddCardFinish,final CardView CardToAdd,final Pane pToMove)
    {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2.0), pToMove);
        final double x = pToMove.getLayoutX();
        final double y = pToMove.getLayoutY();
        translateTransition.setFromX(0);
        translateTransition.setFromY(0);
        translateTransition.setToX(ParticipantImage.getParent().getLayoutX() - x);
        translateTransition.setToY(ParticipantImage.getParent().getLayoutY() + y + pToMove.getMaxHeight()+200);
        
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
        for (Entry<Hand,Pane> entry: HandView.entrySet()) {       
            entry.getValue().getChildren().clear();            
        }
        Hands.clear();
        Hands.addAll(OrderHand);
        HandView.clear();
        
        lblMessage.setText("");
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

    public void PrintMessage(String Message) {
        lblMessage.setText(Message);
    }
}
