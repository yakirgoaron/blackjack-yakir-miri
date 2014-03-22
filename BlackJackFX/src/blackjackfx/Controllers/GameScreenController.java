/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.GameEngine;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author yakir
 */
public class GameScreenController implements Initializable 
{
    
    private GameEngine BJGame;
    @FXML
    private ImageView DealerImage;
    @FXML
    private HBox DealerCards;
    @FXML
    private ImageView Player1;
    @FXML
    private ImageView Player2;
    @FXML
    private ImageView Player3;
    @FXML
    private ImageView Player4;
    @FXML
    private ImageView Player5;
    @FXML
    private ImageView Player6;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }   
    
    public void setBJGame(GameEngine BJGame) {
        this.BJGame = BJGame;
    }
    
}
