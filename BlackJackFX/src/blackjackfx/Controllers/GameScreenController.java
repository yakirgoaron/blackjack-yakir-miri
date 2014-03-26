/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.Bid;
import EngineLogic.Communicable;
import EngineLogic.Communicable.PlayerAction;
import EngineLogic.GameEngine;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.Player;
import blackjackfx.Events;
import blackjackfx.PlayerView;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author yakir
 */
public class GameScreenController implements Initializable
{
    
    private GameEngine BJGame;
    @FXML
    private HBox DealerCards;
    @FXML
    private Pane Player1;
    @FXML
    private Pane Player2;
    @FXML
    private Pane Player3;
    @FXML
    private Pane Player4;
    @FXML
    private Pane Player5;
    @FXML
    private Pane Player6;
    @FXML
    private Pane DealerInfo;
    @FXML
    private Button btnDouble;
    @FXML
    private Button btnHit;
    @FXML
    private Button btnSplit;
    @FXML
    private Button btnStay;
    
    private SimpleObjectProperty<PlayerAction> plAction;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnDouble.setVisible(false);
        btnHit.setVisible(false);
        btnSplit.setVisible(false);
        btnStay.setVisible(false);
        plAction = new SimpleObjectProperty<>();
    }   
    
    public void ShowActions()
    {
       btnDouble.setVisible(true);
        btnHit.setVisible(true);
        btnSplit.setVisible(true);
        btnStay.setVisible(true); 
    }
    
    
    public void setBJGame(GameEngine BJGame) {
        this.BJGame = BJGame;
    }

     public SimpleObjectProperty<PlayerAction> getPlayerActionType() {
        return plAction;
    }
    
    @FXML
    private void StartRound(ActionEvent event) 
    {
        new Events(BJGame,this).start();
    }

    @FXML
    private void DoublePress(ActionEvent event) {
        synchronized(plAction)
        {
            plAction.set(PlayerAction.DOUBLE);
            plAction.notify();
        }
    }

    @FXML
    private void HitPress(ActionEvent event) {
        plAction.set(PlayerAction.HIT);
    }

    @FXML
    private void SplitPress(ActionEvent event) {
        plAction.set(PlayerAction.SPLIT);
    }

    @FXML
    private void StayPress(ActionEvent event) {
        plAction.set(PlayerAction.STAY);
    }
    
    
    
}
