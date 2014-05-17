/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;


import blackjackfx.ServerClasses.Events;
import game.client.ws.DuplicateGameName_Exception;
import game.client.ws.InvalidParameters_Exception;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author yakir
 */
public class CreateGameScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Events BjGame;
    private final int MaxPlayerCount = 6;
    private SimpleBooleanProperty finishedInit;

    @FXML
    private Label errorMessageLabel;
    private final int MaxPlayerName = 10;
    @FXML
    private Button BtnCreateGame;
    @FXML
    private ComboBox<Integer> cbCompPlayerCount;
    @FXML
    private TextField txtGameName;
    @FXML
    private ComboBox<Integer> cbHumanPlayerCount;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       cbHumanPlayerCount.setValue(1);
       cbCompPlayerCount.setValue(1);
       cbCompPlayerCount.valueProperty().addListener(new ChangeListener<Integer>() {

           @Override
           public void changed(ObservableValue<? extends Integer> ov, Integer t, Integer t1) {
               showError("");
               HandleCreateGameBtn();
           }
       });
       
       cbHumanPlayerCount.valueProperty().addListener(new ChangeListener<Integer>() {

           @Override
           public void changed(ObservableValue<? extends Integer> ov, Integer t, Integer t1) {
               showError("");
               HandleCreateGameBtn();
           }
       });
       
       txtGameName.visibleProperty().set(true);
       BtnCreateGame.disableProperty().set(true);
       finishedInit = new SimpleBooleanProperty(false);
       
       txtGameName.textProperty().addListener(new ChangeListener<String>() {

           @Override
           public void changed(ObservableValue<? extends String> ov, String t, String t1) {
               showError("");
               onGameNameChanged();
           }

       });
    }
   
    private void onGameNameChanged() {
        txtGameName.setText(txtGameName.getText().trim());
        
        if (txtGameName.getText().isEmpty())
            BtnCreateGame.disableProperty().set(true); 
        else
        {
            HandleLength();
            HandleCreateGameBtn();
        }
    }
    
    private void HandleCreateGameBtn()
    {
        if ( (!txtGameName.getText().isEmpty()) &&
              (cbCompPlayerCount.getValue() + cbHumanPlayerCount.getValue() <= 
                MaxPlayerCount))
            BtnCreateGame.disableProperty().set(false);
        else{
            BtnCreateGame.disableProperty().set(true);
            showError("Max player count is " + MaxPlayerCount);
        }     
    }

    
    
    private void HandleLength() {
       String Text = txtGameName.getText();
        if (Text.length() > MaxPlayerName)
        {
            String PlayerName = Text.substring(0, MaxPlayerName);
            txtGameName.setText(PlayerName);
        }
    }
    
    public SimpleBooleanProperty getFinishedInit() {
        return finishedInit;
    }
    
    @FXML
    public void TextNameAction(ActionEvent event){
        if (!BtnCreateGame.disableProperty().get())
            OnCreateGame(event);
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
        BtnCreateGame.setDisable(true);           
    }
    
    public void ChangeTextEnable()
    {
        this.txtGameName.visibleProperty().set(!this.txtGameName.visibleProperty().get());
    }
    
    public void EnableTextName()
    {
        this.txtGameName.visibleProperty().set(true);
    }
    
    public void DisableTextName()
    {
        this.txtGameName.visibleProperty().set(false);
    }
    
    public void setBjGame(Events BjGame) {
        this.BjGame = BjGame;
    }

    @FXML
    private void OnCreateGame(ActionEvent event) {
        try 
        {
            BjGame.CreateGame(txtGameName.getText(), cbHumanPlayerCount.getValue(),
                    cbCompPlayerCount.getValue());
            finishedInit.set(true);
        } 
        catch (DuplicateGameName_Exception | InvalidParameters_Exception ex) 
        {
           showError(ex.getMessage());
        }
        
    }
    
}
