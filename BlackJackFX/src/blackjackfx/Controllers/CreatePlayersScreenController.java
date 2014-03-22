/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.GameEngine;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       //this.BtnAdd.onActionProperty().addListener(new );
    }    
    
    @FXML
    public void AddPlayer()
    {
        GameEngine eng = new GameEngine();
        try {
            eng.AddPlayer("Hello");
            eng.AddPlayer();
        }
        catch (TooManyPlayersException ex) 
        {
            Logger.getLogger(CreatePlayersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void setBjGame(GameEngine BjGame) {
        this.BjGame = BjGame;
    }
}
