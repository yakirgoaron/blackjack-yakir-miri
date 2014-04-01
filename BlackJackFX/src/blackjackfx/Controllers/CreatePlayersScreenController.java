/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.GameEngine;
import blackjackfx.PlayerView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author yakir
 */
public class CreatePlayersScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private GameEngine BjGame;
    private Boolean IsHuman;
    
    @FXML
    private Pane PlayerIn;
    @FXML
    private TextField TextName;
    @FXML
    private Button BtnAdd;
    @FXML
    private Button BtnStart;
    @FXML
    private Label errorMessageLabel;

    private SimpleBooleanProperty finishedInit;
    @FXML
    private Label lblPlayerName;
    @FXML
    private Label lblPlayersJoined;
    @FXML
    private ComboBox<?> cbPlayerType;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       /*this.BtnAdd.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               AddPlayer();
            }} );*/
       TextName.visibleProperty().set(false);
       BtnStart.disableProperty().set(true);
       lblPlayerName.visibleProperty().set(false);
       lblPlayersJoined.visibleProperty().set(false);
       BtnAdd.disableProperty().set(true);
       finishedInit = new SimpleBooleanProperty(false);
    }
   
    @FXML
    public void SetPlayerType(ActionEvent t) {      
       BtnAdd.disableProperty().set(false); 
       
        if (cbPlayerType.getValue().toString().equals("Human")){
            IsHuman = true; 
            EnableTextName();
            TextName.requestFocus();
            EnableLblPlayerName();
        }
        else{
            IsHuman = false;           
            DisableTextName();
            DisableLblPlayerName();
        }
    }
    
    public SimpleBooleanProperty getFinishedInit() {
        return finishedInit;
    }
    
    @FXML
    public void AddPlayer(ActionEvent event)
    {
        String PlayerName = "";
        
        try 
        {
            if(IsHuman)
            {
                PlayerName = TextName.getText();
                BjGame.AddPlayer(PlayerName);
            }
            else
            {
                BjGame.AddPlayer();
            }
           
            PlayerView playerView = new PlayerView(PlayerName, this.IsHuman);
            PlayerIn.getChildren().add(playerView);
            this.TextName.clear();
            BtnStart.disableProperty().set(false);
        }
        catch (TooManyPlayersException ex) 
        {
            showError(ex.toString());
        }
        
    }
    private void showError(String message) 
    {
        errorMessageLabel.textProperty().setValue(message);
        FadeTransition animation = FadeTransitionBuilder.create()
                    .node(errorMessageLabel)
                    .duration(Duration.seconds(0.3))
                    .fromValue(0.0)
                    .toValue(1.0)
                    .build();
        animation.play();
        BtnAdd.setDisable(true);
            
    }
    public void ChangeTextEnable()
    {
        this.TextName.visibleProperty().set(!this.TextName.visibleProperty().get());
    }
    
    public void EnableTextName()
    {
        this.TextName.visibleProperty().set(true);
    }
    
    public void DisableTextName()
    {
        this.TextName.visibleProperty().set(false);
    }
    public void ChangeLblPlayerNameEnable(){
        this.lblPlayerName.visibleProperty().set(!this.lblPlayerName.visibleProperty().get());
    }
    
    public void EnableLblPlayerName(){
        this.lblPlayerName.visibleProperty().set(true);
    }
    
    public void DisableLblPlayerName(){
        this.lblPlayerName.visibleProperty().set(false);
    }
    public void ChangeLblPlayerJoinedEnable(){
          this.lblPlayersJoined.visibleProperty().set(!this.lblPlayersJoined.visibleProperty().get());
    }
    
    public void EnableLblPlayerJoined(){
        this.lblPlayerName.visibleProperty().set(true);
    }
    
    public void DisableLblPlayerJoined(){
        this.lblPlayerName.visibleProperty().set(false);
    }
    
    
    
    public void setBjGame(GameEngine BjGame) {
        this.BjGame = BjGame;
    }

    @FXML
    protected void OnStartGame(ActionEvent event) 
    {
        finishedInit.set(true);
    }
}
