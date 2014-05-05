/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;


import blackjackfx.ServerClasses.Events;
import blackjackfx.ParticipantContainer;
import blackjackfx.PlayerContainer;
import blackjackfx.ServerClasses.Card;
import blackjackfx.ServerClasses.PlayerAction;
import blackjackfx.ServerClasses.PlayerInfo;
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
    private Events GameEvents;
    private HashMap<String, ParticipantContainer> Players;
    private SimpleObjectProperty<PlayerAction> plAction;
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
    private Label MsgLable;
      //  private Label lblPlayerEndRound;
    @FXML
    private Button btnRResign;

    @FXML
    private Pane apPlayer1;






    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnDouble.setVisible(false);
        btnHit.setVisible(false);
        btnSplit.setVisible(false);
        btnStay.setVisible(false);
   //     lblPlayerEndRound.setVisible(false);
        plAction = new SimpleObjectProperty<>();
        DoesPlayerContinue = new SimpleBooleanProperty();
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
        btnRResign.setDisable(false);
    }
    
    public void ShowActions()
    {
       btnDouble.setVisible(true);
        btnHit.setVisible(true);
        btnSplit.setVisible(true);
        btnStay.setVisible(true); 
        btnRResign.setDisable(true);
    }
    
    public void setGameEvents(Events BJGame) {
        GameEvents = BJGame; 
        GameEvents.SetController(this);
        GameEvents.start();
    }
    
    public void DisplayBid(String currBid,PlayerInfo currPlayer)
    {
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (currPlayer.getName().equals("Dealer"))
        {
            Players.get("Dealer").PrintHandInfo(currBid, currPlayer.getBets().get(0).getBetCards());
        }
        else{
        ((PlayerContainer)Players.get(currPlayer.getName())).PrintBidInfo(currPlayer.getName() + "1", currPlayer.getBets().get(0));
        ((PlayerContainer)Players.get(currPlayer.getName())).PrintBidInfo(currPlayer.getName() + "2", currPlayer.getBets().get(1));
        }
        /*for (Bid bid : currPlayer.getBids()) {
            ((PlayerContainer)Players.get(currPlayer)).PrintBidInfo(bid);
            
            Players.get(currPlayer).ClearGlowHandInfo(bid);
        }*/
        
        Players.get(currPlayer.getName()).GlowHandInfo(currBid);
    }
    
    public void DisplayHand(String currHand,List<Card> currPlayer)
    {
         Players.get(currPlayer).PrintHandInfo(currHand,currPlayer);   
    }
    
    public void DisplayPlayer(PlayerInfo dispPlayer)
    {       
        ClearEffects();
        Players.get(dispPlayer.getName()).PrintPlayerInfo(dispPlayer);
    }
    
    public void DiplayEffect(String playerTurn)
    {
        ClearEffects();
        Players.get(playerTurn).SetParticipantEffect();
    }
    
    public void ClearEffects(){
        for (Entry<String, ParticipantContainer> entry: Players.entrySet()){
            entry.getValue().ClearEffects();
        }
    }
    
    public SimpleObjectProperty<PlayerAction> getPlayerActionType() {
        return plAction;
    }
    
    public void GetFilePathToSave()
    {
        /*
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
            this.FlPath.set(flToSave.getPath() + ".xml");  */    
    }
    
        private void StartRound(ActionEvent event) 
    {
       InitPlayers();       
       /*GameEvents.getGameEnded().addListener(new ChangeListener<Boolean>() {

           @Override
           public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
               if (t1)
                   GameEnded.set(true);
           }
       });*/
       GameEvents.setDaemon(true);
       GameEvents.start();       
       HideBidWindow.set(false);
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
             plAction.set(PlayerAction.STAND);
             plAction.notify();
        }
        ChangeVisibleAction();
    }

    private void AssignPlayerToUI(String Name,int Index)
    {
        Scene scene = apPlayer1.getScene();
        
        if (Name.equals("Dealer")){                 
            HBox DealerHand = (HBox) scene.lookup("#vbxDealerHand");
            Pane DealerImage = (Pane) scene.lookup("#pDealerPane");
            Pane DeckPlace = (Pane) scene.lookup("#pDeckPlaceD");
            Label DealerMessage = (Label) scene.lookup("#lblDealerMessage");
            ParticipantContainer DealerCont =
                 new ParticipantContainer(DealerHand, DealerImage, DealerMessage, DeckPlace);
            Players.put(Name, DealerCont);
        }
        else{
            VBox FirstBid = (VBox) scene.lookup("#vbxPlayerBid" + (Index+1) + "a");
            VBox SecondBid = (VBox) scene.lookup("#vbxPlayerBid" + (Index+1) + "b");
            Pane PlayerImage = (Pane) scene.lookup("#pPlayerPane" + (Index+1));
            Label Bid1 = (Label) scene.lookup("#pPlayerBid" + (Index+1));
            Label Bid2 = (Label) scene.lookup("#pPlayerBid" + (Index+1) + "2");
            Label Money = (Label) scene.lookup("#lblPlayerMoney" + (Index+1));
            Pane DeckPlace = (Pane) scene.lookup("#pDeckPlace" + (Index+1));
            Label PlayerMessage = (Label) scene.lookup("#lblPlayerMessage" + (Index+1));

            PlayerContainer playerCont = 
                       new PlayerContainer(FirstBid, SecondBid, PlayerImage,
                                           Bid1,Bid2, Money, 
                                           PlayerMessage, DeckPlace);
        
            Players.put(Name, playerCont); 
        }
    }
    public void AddPlayerToGame(List<PlayerInfo> GamePlayers)
    {
        int CurrentPlayer = 0;
        
        for (PlayerInfo playerDetails : GamePlayers)
        {
            String Name = playerDetails.getName();
            AssignPlayerToUI(Name,CurrentPlayer);            
            
            if (!playerDetails.getName().equals("Dealer")){
                Players.get(Name).PrintPlayerInfo(playerDetails);
                CurrentPlayer++;
            }
        }
    }
    
    private void InitPlayers() {
       List<PlayerInfo> GamePlayers = GameEvents.GetPlayersInGame();
      
       Scene scene = apPlayer1.getScene();
       
       for (int i=0; i< GamePlayers.size(); i++)
       {
           AssignPlayerToUI(GamePlayers.get(i).getName(),i);           
       }
       
       HBox DealerHand = (HBox) scene.lookup("#vbxDealerHand");
       Pane DealerImage = (Pane) scene.lookup("#pDealerPane");
       Pane DeckPlace = (Pane) scene.lookup("#pDeckPlaceD");
       Label DealerMessage = (Label) scene.lookup("#lblDealerMessage");
       ParticipantContainer DealerCont =
            new ParticipantContainer(DealerHand, DealerImage, DealerMessage, DeckPlace);
       //TODO DEAL WITH DEALER WHEN WE GET ONE
        //Players.put(BJGame.getGameDealer(), DealerCont);
    }
    
    public void DisplayMessage(String Msg)
    {
        this.MsgLable.setText(Msg);
    }

    public void ClearTable() {
        
        for (Entry<String, ParticipantContainer> entry: Players.entrySet()){
            entry.getValue().ClearCards();
        }
        
        MsgLable.setText("");
    }

    public void ShowPlayers(ArrayList<PlayerInfo> GamePlayers) {
        
        for (PlayerInfo player: GamePlayers){
            PlayerContainer playerCont = (PlayerContainer) Players.get(player);
            playerCont.PrintPlayerInfo(player);
            playerCont.ClearEffects();
            
            
            playerCont.PrintBidInfo(player.getName() + "1", player.getBets().get(0));
            
            playerCont.PrintBidInfo(player.getName() + "2", player.getBets().get(1));
        }
    }

    /*
    public void ShowPlayerContGame(String playerName) {
        lblPlayerEndRound.setVisible(true);
        lblPlayerEndRound.toFront();
        String text = "player " + playerName + "\n" + "what do you "
                       + "\n wish to do?";
        lblPlayerEndRound.setText(text);
        btnPlayerContinue.setVisible(true);
        btnPlayerExit.setVisible(true);
    }*/

    public SimpleBooleanProperty getDoesPlayerContinue() {
        return DoesPlayerContinue;
    }

    /*
        private void PlayerContGame(ActionEvent event) {
        
        synchronized(DoesPlayerContinue)
        {
            DoesPlayerContinue.set(true);
            DoesPlayerContinue.notify();
        }
        ChangeVisiblePlayerRoundEnd();
    }

        private void PlayerExitGame(ActionEvent event) {
        synchronized(DoesPlayerContinue)
        {
            DoesPlayerContinue.set(false);
            DoesPlayerContinue.notify();
        }
        ChangeVisiblePlayerRoundEnd();
    }*/

    public void RemovePlayer(String PlayerName) {
        Players.get(PlayerName).RemovePlayer();
        Players.remove(PlayerName);
    }

    public void PrintPlayerMessage(PlayerInfo ParPlayer, String Message) {
        Players.get(ParPlayer.getName()).PrintMessage(Message);
    }

    @FXML
    private void Resign(ActionEvent event) {
        GameEvents.PlayerResign();
    }

    public void DisableResign() {
        btnRResign.setVisible(false);
    }
       
}
