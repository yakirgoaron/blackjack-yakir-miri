/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackfx.Controllers;

import EngineLogic.GameEngine;
import GameEnums.MainMenu;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author miri
 */
public class MainWindowController implements Initializable {
    
    @FXML
    private Button btnNewGame;
    @FXML
    private Font x1;
    @FXML
    private Button btnLoadGame;
    
    private SimpleObjectProperty<MainMenu> GameInitType;
    @FXML
    private Label ErrorMessage;
    
   
    
    //private Prope Load;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GameInitType = new SimpleObjectProperty<>();
        
        this.btnLoadGame.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                OnStartButtonAction(MainMenu.LOAD_GAME);
               
        }});
        
        this.btnNewGame.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                OnStartButtonAction(MainMenu.NEW_GAME);
               
        }});
        
    }
              
    public void SetErrorMessage(String Message)   
    {
        ErrorMessage.setText(Message);
    }
    private void OnStartButtonAction(MainMenu MainMenuType) {
        GameInitType.set(MainMenuType);
        GameInitType.set(null);
    }

    public SimpleObjectProperty<MainMenu> getGameInitType() {
        return GameInitType;
    }
    
    
        
      
   
}
