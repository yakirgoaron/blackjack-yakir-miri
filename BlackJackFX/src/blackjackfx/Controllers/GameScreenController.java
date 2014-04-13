/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.Bid;
import EngineLogic.Communicable.PlayerAction;
import EngineLogic.Communicable.RoundAction;
import GameEnums.SaveOptions;
import EngineLogic.GameEngine;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.Player;
import blackjackfx.Events;
import blackjackfx.ParticipantContainer;
import blackjackfx.PlayerContainer;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author yakir
 */
public class GameScreenController implements Initializable
{    
    private GameEngine BJGame;
    private Events GameEvents;
    private HashMap<GameParticipant, ParticipantContainer> Players;
    private SimpleObjectProperty<PlayerAction> plAction;
    private SimpleObjectProperty<SaveOptions> plSave;
    private SimpleObjectProperty<RoundAction> RoundChoice;
    private SimpleStringProperty FlPath;
    private SimpleBooleanProperty HideBidWindow;
    private SimpleBooleanProperty DoesPlayerContinue;
    private SimpleBooleanProperty GameEnded;
    
    @FXML
    private Button btnDouble;
    @FXML
    private Button btnHit;
    @FXML
    private Button btnSplit;
    @FXML
    private Button btnStay; 
    @FXML
    private Button btnContinue;
    @FXML
    private Button btnSaveGame;
    @FXML
    private Button btnExitGame;  
    @FXML
    private AnchorPane apPlayer1;
    @FXML
    private Label MsgLable;
    @FXML
    private Button btnStartRound;
    @FXML
    private Button btnPlayerContinue;
    @FXML
    private Button btnPlayerExit;
    @FXML
    private Label lblPlayerEndRound;
    @FXML
    private Button btnSaveOption;
    @FXML
    private Button btnSaveAsOption;

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
        btnSaveOption.setVisible(false);
        btnSaveAsOption.setVisible(false);
        btnPlayerContinue.setVisible(false);
        btnPlayerExit.setVisible(false);
        lblPlayerEndRound.setVisible(false);
        plAction = new SimpleObjectProperty<>();
        plSave = new SimpleObjectProperty<>();
        DoesPlayerContinue = new SimpleBooleanProperty();
        RoundChoice = new SimpleObjectProperty<>();
        FlPath = new SimpleStringProperty();
        Players = new HashMap<>();
        HideBidWindow = new SimpleBooleanProperty(true);
        GameEnded = new SimpleBooleanProperty();
    }  
    
    public SimpleBooleanProperty GetHideBidWindow()
    {
        return HideBidWindow;
    }
    
    public SimpleStringProperty GetPath()
    {
        return this.FlPath;
    }
    
    public void ChangeVisibleAction()
    {
        btnDouble.setVisible(!btnDouble.isVisible());
        btnHit.setVisible(!btnHit.isVisible());
        btnSplit.setVisible(!btnSplit.isVisible());
        btnStay.setVisible(!btnStay.isVisible());
    }
    
    public void ChangeVisibleEnd()
    {
        btnContinue.setVisible(!btnContinue.isVisible());
        btnSaveGame.setVisible(!btnSaveGame.isVisible());
        btnExitGame.setVisible(!btnExitGame.isVisible());
    }
    
    public void ChangeVisiblePlayerRoundEnd(){
        lblPlayerEndRound.setVisible(false);
        lblPlayerEndRound.toBack();
        btnPlayerContinue.setVisible(false);
        btnPlayerExit.setVisible(false);
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
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Bid bid : currPlayer.getBids()) {
            ((PlayerContainer)Players.get(currPlayer)).PrintBidInfo(bid);
            
            Players.get(currPlayer).ClearGlowHandInfo(bid);
        }
        
        Players.get(currPlayer).GlowHandInfo(currBid);
    }
    
    public void DisplayHand(Hand currHand,GameParticipant currPlayer)
    {
         Players.get(currPlayer).PrintHandInfo(currHand);   
    }
    
    public void DisplayPlayer(Player dispPlayer)
    {       
        ClearEffects();
        Players.get(dispPlayer).PrintPlayerInfo(dispPlayer);
    }
    
    public void ClearEffects(){
        for (Entry<GameParticipant, ParticipantContainer> entry: Players.entrySet()){
            entry.getValue().ClearEffects();
        }
    }
    
    public SimpleObjectProperty<PlayerAction> getPlayerActionType() {
        return plAction;
    }

    public SimpleObjectProperty<RoundAction> getRoundChoice() {
        return RoundChoice;
    }
    
    public void GetFilePathToSave()
    {
        FileChooser flChose = new FileChooser();
        File flToSave = null;
        flChose.setTitle("Choose a path to save game");
        flChose.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        while (flToSave == null)
            flToSave = flChose.showSaveDialog(apPlayer1.getScene().getWindow());
        
        if(flToSave.getPath().endsWith(".xml"))         
            this.FlPath.set(flToSave.getPath());      
        else
            this.FlPath.set(flToSave.getPath() + ".xml");      
    }
    
    @FXML
    private void StartRound(ActionEvent event) 
    {
       InitPlayers();       
       GameEvents = new Events(BJGame,this);
       GameEvents.getGameEnded().addListener(new ChangeListener<Boolean>() {

           @Override
           public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
               if (t1)
                   GameEnded.set(true);
           }
       });
       GameEvents.setDaemon(true);
       GameEvents.start();       
       HideBidWindow.set(false);
       btnStartRound.setDisable(true);
    }

    public SimpleBooleanProperty getGameEnded() {
        return GameEnded;
    }
    

    @FXML
    private void DoublePress(ActionEvent event) {
        synchronized(plAction)
        {
            plAction.set(PlayerAction.DOUBLE);
            plAction.notify();
        }
        ChangeVisibleAction();
    }

    @FXML
    private void HitPress(ActionEvent event) {
        synchronized(plAction)
        {
             plAction.set(PlayerAction.HIT);
             plAction.notify();
        }
        ChangeVisibleAction();
       
    }

    @FXML
    private void SplitPress(ActionEvent event) {
        synchronized(plAction)
        {
             plAction.set(PlayerAction.SPLIT);
             plAction.notify();
        }
        ChangeVisibleAction();
    }

    @FXML
    private void StayPress(ActionEvent event) {
        synchronized(plAction)
        {
             plAction.set(PlayerAction.STAY);
             plAction.notify();
        }
        ChangeVisibleAction();
    }

    @FXML
    private void ContinuePress(ActionEvent event) {
        synchronized (RoundChoice){
            RoundChoice.set(RoundAction.CONTINUE_GAME);
            RoundChoice.notify();
        }
        ChangeVisibleEnd();
    }

    @FXML
    private void SavePress(ActionEvent event) {
         synchronized (RoundChoice){
            RoundChoice.set(RoundAction.SAVE_GAME);
            RoundChoice.notify();
        }
         ChangeVisibleEnd();
    }

    @FXML
    private void ExitPress(ActionEvent event) {
         synchronized (RoundChoice){
            RoundChoice.set(RoundAction.EXIT_GAME);
            RoundChoice.notify();
        }
        ChangeVisibleEnd();
    }

    private void InitPlayers() {
       ArrayList<Player> GamePlayers = BJGame.getGamePlayers();
      
       Scene scene = apPlayer1.getScene();
       
       for (int i=0; i< GamePlayers.size(); i++){
                   
           VBox FirstBid = (VBox) scene.lookup("#vbxPlayerBid" + (i+1) + "a");
           VBox SecondBid = (VBox) scene.lookup("#vbxPlayerBid" + (i+1) + "b");
           Pane PlayerImage = (Pane) scene.lookup("#pPlayerPane" + (i+1));
           Label Bid1 = (Label) scene.lookup("#pPlayerBid" + (i+1));
           Label Bid2 = (Label) scene.lookup("#pPlayerBid" + (i+1) + "2");
           Label Money = (Label) scene.lookup("#lblPlayerMoney" + (i+1));
           Pane DeckPlace = (Pane) scene.lookup("#pDeckPlace" + (i+1));
           Label PlayerMessage = (Label) scene.lookup("#lblPlayerMessage" + (i+1));
           
           PlayerContainer playerCont = 
                   new PlayerContainer(FirstBid, SecondBid, PlayerImage,
                                       Bid1,Bid2, Money, 
                                       PlayerMessage, DeckPlace);
           Players.put(GamePlayers.get(i), playerCont);              
       }
       
       HBox DealerHand = (HBox) scene.lookup("#vbxDealerHand");
       Pane DealerImage = (Pane) scene.lookup("#pDealerPane");
       Pane DeckPlace = (Pane) scene.lookup("#pDeckPlaceD");
       Label DealerMessage = (Label) scene.lookup("#lblDealerMessage");
       ParticipantContainer DealerCont =
            new ParticipantContainer(DealerHand, DealerImage, DealerMessage, DeckPlace);
       Players.put(BJGame.getGameDealer(), DealerCont);
    }
    
    public void DisplayMessage(String Msg)
    {
        this.MsgLable.setText(Msg);
    }

    public void ClearTable() {
        
        for (Entry<GameParticipant, ParticipantContainer> entry: Players.entrySet()){
            entry.getValue().ClearCards();
        }
        
        MsgLable.setText("");
    }

    public void ShowPlayers(ArrayList<Player> GamePlayers) {
        
        for (Player player: GamePlayers){
            PlayerContainer playerCont = (PlayerContainer) Players.get(player);
            playerCont.PrintPlayerInfo(player);
            playerCont.ClearEffects();
           
            List<Bid> PlayerBids = player.getBids();
            
            for (Bid bid: PlayerBids)
                playerCont.PrintBidInfo(bid);
        }
    }

    public void ShowPlayerContGame(String playerName) {
        lblPlayerEndRound.setVisible(true);
        lblPlayerEndRound.toFront();
        String text = "player " + playerName + "\n" + "what do you "
                       + "\n wish to do?";
        lblPlayerEndRound.setText(text);
        btnPlayerContinue.setVisible(true);
        btnPlayerExit.setVisible(true);
    }

    public SimpleBooleanProperty getDoesPlayerContinue() {
        return DoesPlayerContinue;
    }

    @FXML
    private void PlayerContGame(ActionEvent event) {
        
        synchronized(DoesPlayerContinue)
        {
            DoesPlayerContinue.set(true);
            DoesPlayerContinue.notify();
        }
        ChangeVisiblePlayerRoundEnd();
    }

    @FXML
    private void PlayerExitGame(ActionEvent event) {
        synchronized(DoesPlayerContinue)
        {
            DoesPlayerContinue.set(false);
            DoesPlayerContinue.notify();
        }
        ChangeVisiblePlayerRoundEnd();
    }

    public void RemovePlayer(Player player) {
        Players.get(player).RemovePlayer();
        Players.remove(player);
    }

    public void PrintPlayerMessage(GameParticipant ParPlayer, String Message) {
        Players.get(ParPlayer).PrintMessage(Message);
    }

    public SimpleObjectProperty<SaveOptions> getPlayerSaveType() {
        return plSave;
    }

    public void ShowSaveOptions() {
        btnSaveAsOption.setVisible(true);
        btnSaveAsOption.toFront();
        btnSaveOption.setVisible(true);
        btnSaveOption.toFront();
    }

    @FXML
    private void SaveOptionPress(ActionEvent event) {
        synchronized(plSave)
        {
            plSave.set(SaveOptions.SAVE);
            plSave.notify();
        }
        ChangeVisibleSaveOptions();
    }

    @FXML
    private void SaveAsOptionPress(ActionEvent event) {
        synchronized(plSave)
        {
            plSave.set(SaveOptions.SAVE_AS);
            plSave.notify();
        }
        ChangeVisibleSaveOptions();
    }

    private void ChangeVisibleSaveOptions() {
        btnSaveAsOption.setVisible(false);
        btnSaveAsOption.toBack();
        btnSaveOption.setVisible(false);
        btnSaveOption.toBack();
    }
       
}
