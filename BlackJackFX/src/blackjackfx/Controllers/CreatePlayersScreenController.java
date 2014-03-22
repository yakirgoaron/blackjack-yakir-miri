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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    @FXML
    private Pane PlayerIn;
    @FXML
    private TextField TextName;
    @FXML
    private Button BtnAdd;
    @FXML
    private CheckBox IsHuman;
    @FXML
    private Button BtnStart;
    @FXML
    private Label errorMessageLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       this.BtnAdd.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               AddPlayer();
            }} );
       this.TextName.visibleProperty().set(false);
       this.IsHuman.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               ChangeTextEnable();
            }});
    }
    
    @FXML
    public void AddPlayer()
    {
        try 
        {
            if(this.IsHuman.isSelected())
            {
                BjGame.AddPlayer(TextName.getText());
            }
            else
            {
                BjGame.AddPlayer();
            }
            this.TextName.clear();
            PlayerView playerView = new PlayerView(TextName.getText(), this.IsHuman.isSelected());
            PlayerIn.getChildren().add(playerView);
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
    }
    public void ChangeTextEnable()
    {
        this.TextName.visibleProperty().set(!this.TextName.visibleProperty().get());
    }
    
    
    public void setBjGame(GameEngine BjGame) {
        this.BjGame = BjGame;
    }
}
