/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import EngineLogic.Player;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author yakir
 */
public class BidInputController implements Initializable {
    @FXML
    private Slider SdBidAmount;
    @FXML
    private Button btnFinish;
    @FXML
    private Label lblPlayerName;
    @FXML
    private TextField Amount;
    
    private Player plCurrent;
    
    private SimpleDoubleProperty dblAmount;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dblAmount = new SimpleDoubleProperty();
    }    
    
    public SimpleDoubleProperty GetNumberBid()
    {
        return this.dblAmount;
    }
    
    public void SetPlayer(Player current)
    {
        this.plCurrent = current;
        lblPlayerName.setText(current.getName());
        SdBidAmount.setMax(current.getMoney());
        SdBidAmount.setMin(1.0);
        SdBidAmount.setShowTickLabels(true);
        SdBidAmount.setShowTickMarks(true);
        SdBidAmount.setValue(1.0);
        Amount.setText(Double.toString(SdBidAmount.getValue()));
    }
    
    @FXML
    private void Finish(ActionEvent event) {
        synchronized(this.dblAmount)
        {
            this.dblAmount.set(SdBidAmount.getValue());
            this.dblAmount.notify();
        }
        
    }
    
}
