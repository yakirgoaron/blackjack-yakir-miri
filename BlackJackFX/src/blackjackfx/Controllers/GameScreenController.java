/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.Bid;
import EngineLogic.Communicable;
import EngineLogic.Communicable.PlayerAction;
import EngineLogic.Communicable.RoundAction;
import EngineLogic.GameEngine;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.HumanPlayer;
import EngineLogic.Player;
import blackjackfx.Events;
import blackjackfx.PlayerContainer;
import blackjackfx.PlayerView;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    private Pane DealerInfo;
    @FXML
    private Button btnDouble;
    @FXML
    private Button btnHit;
    @FXML
    private Button btnSplit;
    @FXML
    private Button btnStay;
    private HashMap<Player, PlayerContainer> Players;
    private SimpleObjectProperty<PlayerAction> plAction;
    private SimpleObjectProperty<RoundAction> RoundChoice;
    @FXML
    private Button btnContinue;
    @FXML
    private Button btnSaveGame;
    @FXML
    private Button btnExitGame;
    private Events GameEvents;
    @FXML
    private AnchorPane apPlayer1;
    @FXML
    private HBox hbxPlayers;
    @FXML
    private VBox vbxPlayerBid1b;
    @FXML
    private VBox vbxPlayerBid1a;
    @FXML
    private Pane pPlayerPane1;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnDouble.setVisible(false);
        btnHit.setVisible(false);
        btnSplit.setVisible(false);
        btnStay.setVisible(false);
        btnContinue.setVisible(false);
        btnSaveGame.setVisible(false);
        btnExitGame.setVisible(false);
        plAction = new SimpleObjectProperty<>();
        RoundChoice = new SimpleObjectProperty<>();
        Players = new HashMap<>();
    }   
    
    public void ShowActions()
    {
       btnDouble.setVisible(true);
        btnHit.setVisible(true);
        btnSplit.setVisible(true);
        btnStay.setVisible(true); 
    }
    
    public void ShowRoundActions(){
        btnContinue.setVisible(true);
        btnSaveGame.setVisible(true);
        btnExitGame.setVisible(true);
    }
    
    
    public void setBJGame(GameEngine BJGame) {
        this.BJGame = BJGame;
        
    }

    public void DisplayPlayer(Player dispPlayer)
    {
      //  Boolean IsHuman = dispPlayer instanceof HumanPlayer;
        Players.get(dispPlayer).PrintPlayerInfo(dispPlayer);
     //   PlayerView plView = new PlayerView(dispPlayer.getName(), IsHuman);
    }
    public SimpleObjectProperty<PlayerAction> getPlayerActionType() {
        return plAction;
    }

    public SimpleObjectProperty<RoundAction> getRoundChoice() {
        return RoundChoice;
    }
    
    
    @FXML
    private void StartRound(ActionEvent event) 
    {
        InitPlayers();
       GameEvents = new Events(BJGame,this);
       GameEvents.start();
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
        synchronized(plAction)
        {
             plAction.set(PlayerAction.HIT);
             plAction.notify();
        }
       
    }

    @FXML
    private void SplitPress(ActionEvent event) {
        synchronized(plAction)
        {
             plAction.set(PlayerAction.SPLIT);
             plAction.notify();
        }
        
    }

    @FXML
    private void StayPress(ActionEvent event) {
        synchronized(plAction)
        {
             plAction.set(PlayerAction.STAY);
             plAction.notify();
        }
        
    }

    @FXML
    private void ContinuePress(ActionEvent event) {
        synchronized (RoundChoice){
            RoundChoice.set(RoundAction.CONTINUE_GAME);
        }
    }

    @FXML
    private void SavePress(ActionEvent event) {
         synchronized (RoundChoice){
            RoundChoice.set(RoundAction.SAVE_GAME);
        }
    }

    @FXML
    private void ExitPress(ActionEvent event) {
         synchronized (RoundChoice){
            RoundChoice.set(RoundAction.EXIT_GAME);
        }
    }

    private void InitPlayers() {
       ArrayList<Player> GamePlayers = BJGame.getGamePlayers();
      
       Scene scene = apPlayer1.getScene();
       
       for (int i=0; i< GamePlayers.size(); i++){
                   
           VBox FirstBid = (VBox) scene.lookup("#vbxPlayerBid" + (i+1) + "a");
           VBox SecondBid = (VBox) scene.lookup("#vbxPlayerBid" + (i+1) + "b");
           Pane PlayerImage = (Pane) scene.lookup("#pPlayerPane" + (i+1));
           PlayerContainer playerCont = 
                   new PlayerContainer(FirstBid, SecondBid, PlayerImage);
           Players.put(GamePlayers.get(i), playerCont);              
       }
    }
    
    
    
}
