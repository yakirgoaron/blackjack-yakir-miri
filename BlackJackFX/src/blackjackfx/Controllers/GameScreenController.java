/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.Bid;
import EngineLogic.Communicable;
import EngineLogic.GameEngine;
import EngineLogic.GameParticipant;
import EngineLogic.Hand;
import EngineLogic.Player;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author yakir
 */
public class GameScreenController implements Initializable ,Communicable
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }   
    
    public void setBJGame(GameEngine BJGame) {
        this.BJGame = BJGame;
    }

    @Override
    public boolean DoesPlayerContinue(Player player) {
        return true;
    }

    @Override
    public String getFilePathForSave() 
    {
        return "Hello";
    }

    @Override
    public PlayerAction GetWantedAction() 
    {
        return PlayerAction.STAY;
    }

    @Override
    public void PrintPlayerInfo(Player PlayerToPrint) 
    {
        
    }

    @Override
    public void PrintBasicPlayerInfo(Player PlayerToPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RoundAction GetFinishRoundAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double GetBidForPlayer(Player BettingPlayer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintBidInfo(Bid BidForPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintHandInfo(Hand HandForPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintParticipnatName(GameParticipant PartToPrint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PrintMessage(String Message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
