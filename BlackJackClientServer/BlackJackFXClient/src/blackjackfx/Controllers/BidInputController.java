/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import blackjackfx.ServerClasses.GameInfo;
import blackjackfx.ServerClasses.PlayerInfo;
import game.client.ws.GameDoesNotExists_Exception;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.util.Duration;

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
    
    private PlayerInfo plCurrent;
    
    private SimpleDoubleProperty dblAmount;
    private SlideBarChange SdeventChange;
    private TextBoxValueChange TxteventChange;
    @FXML
    private Label lblPlayerMoney;
    @FXML
    private Label lblError;
    
    private final double InvalidBid = -5;
    @FXML
    private ProgressBar TimeCountDown;
    private Timer UpdateTimer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dblAmount = new SimpleDoubleProperty(InvalidBid);
        Amount.setText("1");
        SdeventChange = new SlideBarChange();
        TxteventChange = new TextBoxValueChange();
        SdBidAmount.valueProperty().addListener(SdeventChange);
        Amount.textProperty().addListener(TxteventChange);  
        
    }    

    public double getInvalidBid() {
        return InvalidBid;
    }
    
    public SimpleDoubleProperty GetNumberBid()
    {
        return this.dblAmount;
    }

    public void SetNumberBid(double dblAmount) {
        this.dblAmount.set(dblAmount);
    }
    
    
    public void SetPlayer(PlayerInfo current)
    {
        this.plCurrent = current;
        lblPlayerName.setText(current.getName());
        
        
        lblPlayerMoney.setText(Double.toString(current.getMoney()));
        SdBidAmount.setMax(current.getMoney());
        lblPlayerMoney.setText(Double.toString(current.getMoney()));
        SdBidAmount.setMax(current.getMoney());
        
        SdBidAmount.setMin(0.0);
        SdBidAmount.setMajorTickUnit(25);
        SdBidAmount.setMinorTickCount(50);
        SdBidAmount.setBlockIncrement(10);
        SdBidAmount.setValue(1.0);
               
        Amount.setText(Integer.toString((int)SdBidAmount.getValue()));
        TimeCountDown.setProgress(1);
        CreateTimer();
       
    }
    
    @FXML
    private void Finish(ActionEvent event) {
        if(SdBidAmount.getValue() > 0)
        {
            UpdateTimer.cancel();
            synchronized(this.dblAmount)
            {
                this.dblAmount.set(SdBidAmount.getValue());
                this.dblAmount.notify();
            }
        }
        else
        {
            showError("Must enter bid that more than zero");
        }
        
    }
    
    private void UpdateText(DragEvent event) {
        Amount.setText(Double.toString(SdBidAmount.getValue()));
    }
    
    public class SlideBarChange implements ChangeListener<Number>
    {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) 
        {
            Amount.textProperty().removeListener(TxteventChange);
            SdBidAmount.valueProperty().removeListener(SdeventChange);
            Amount.setText(new DecimalFormat("####").format(SdBidAmount.getValue()));
            SdBidAmount.setValue(((int)SdBidAmount.getValue()));
            Amount.textProperty().addListener(TxteventChange);
            SdBidAmount.valueProperty().addListener(SdeventChange);
            btnFinish.setDisable(false);
        }      
    }
    
    public class TextBoxValueChange implements ChangeListener<String>
    {
        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) 
        {
            SdBidAmount.valueProperty().removeListener(SdeventChange);
            try
            {
                Double Temp = Double.parseDouble(t1);
                
                //TODO : CHECK MONEY AFTER ADDED
                if(/*Temp > plCurrent.getMoney() ||*/ Temp < 0)
                    throw new Error();
                SdBidAmount.setValue(Temp);                
                lblError.textProperty().setValue("");
                btnFinish.setDisable(false);
            }
            catch (NumberFormatException e) 
            {
                showError("Only Netural numbers allowed");
                
            } catch (Error ex) {
                showError("Cant bid more than max");
                
            }
            SdBidAmount.valueProperty().addListener(SdeventChange);
        }       
    }
    
    private void showError(String message) 
    {
        lblError.textProperty().setValue(message);
        FadeTransition animation = FadeTransitionBuilder.create()
                    .node(lblError)
                    .duration(Duration.seconds(0.3))
                    .fromValue(0.0)
                    .toValue(1.0)
                    .build();
                 animation.play();
       btnFinish.setDisable(true);
            
    }
    private void CreateTimer()
    {
        UpdateTimer = new Timer(true);
        TimerTask taskUpdate = new TimerTask() {

            @Override
            public void run() 
            {
                    Platform.runLater(new Runnable(){
                                @Override
                                public void run() 
                                { 
                                    TimeCountDown.setProgress(TimeCountDown.getProgress()-0.01);
                                }});  
            }
        };
        
        UpdateTimer.schedule(taskUpdate,0, 100);
    }
    
}
