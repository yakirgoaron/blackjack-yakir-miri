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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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
            PlayerView playerView = new PlayerView(TextName.getText(), this.IsHuman.isSelected());
            PlayerIn.getChildren().add(playerView);
        }
        catch (TooManyPlayersException ex) 
        {
            Logger.getLogger(CreatePlayersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void ChangeTextEnable()
    {
        this.TextName.visibleProperty().set(!this.TextName.visibleProperty().get());
    }
    
    
    public void setBjGame(GameEngine BjGame) {
        this.BjGame = BjGame;
    }
}
