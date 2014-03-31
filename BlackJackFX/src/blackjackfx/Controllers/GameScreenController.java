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
import blackjackfx.ParticipantContainer;
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
import javafx.scene.control.Label;
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
    private Button btnDouble;
    @FXML
    private Button btnHit;
    @FXML
    private Button btnSplit;
    @FXML
    private Button btnStay;
    private HashMap<GameParticipant, ParticipantContainer> Players;
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
    private VBox vbxPlayerBid1b;
    @FXML
    private VBox vbxPlayerBid1a;
    @FXML
    private Pane pPlayerPane1;
    @FXML
    private AnchorPane apPlayer2;
    @FXML
    private VBox vbxPlayerBid2b;
    @FXML
    private VBox vbxPlayerBid2a;
    @FXML
    private Pane pPlayerPane2;
    @FXML
    private AnchorPane apPlayer3;
    @FXML
    private VBox vbxPlayerBid3b;
    @FXML
    private VBox vbxPlayerBid3a;
    @FXML
    private Pane pPlayerPane3;
    @FXML
    private AnchorPane apPlayer4;
    @FXML
    private VBox vbxPlayerBid4b;
    @FXML
    private VBox vbxPlayerBid4a;
    @FXML
    private Pane pPlayerPane4;
    @FXML
    private AnchorPane apPlayer5;
    @FXML
    private VBox vbxPlayerBid5b;
    @FXML
    private VBox vbxPlayerBid5a;
    @FXML
    private Pane pPlayerPane5;
    @FXML
    private AnchorPane apPlayer6;
    @FXML
    private VBox vbxPlayerBid6b;
    @FXML
    private VBox vbxPlayerBid6a;
    @FXML
    private Pane pPlayerPane6;
    @FXML
    private Label MsgLable;
    @FXML
    private Pane PDealerPane;
    @FXML
    private VBox vbxDealerHand;
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
    public void DisplayBid(Bid currBid,Player currPlayer)
    {
        Players.get(currPlayer).PrintHandInfo(currBid);
    }
    public void DisplayHand(Hand currHand,GameParticipant currPlayer)
    {
        Players.get(currPlayer).PrintHandInfo(currHand);
        
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
           ParticipantContainer playerCont = 
                   new ParticipantContainer(FirstBid, SecondBid, PlayerImage);
           Players.put(GamePlayers.get(i), playerCont);              
       }
       
       VBox DealerHand = (VBox) scene.lookup("#vbxDealerHand");
       Pane DealerImage = (Pane) scene.lookup("#pDealerPane");
       ParticipantContainer DealerCont =
            new ParticipantContainer(DealerHand, DealerImage);
       Players.put(BJGame.getGameDealer(), DealerCont);
    }
    
    public void DisplayMessage(String Msg)
    {
        this.MsgLable.setText(Msg);
    }
    
}
