/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackfx.Controllers;

import GameEnums.MainMenu;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author miri
 */
public class MainWindowController implements Initializable {
    
    private SimpleObjectProperty<MainMenu> GameInitType;
     
    @FXML
    private Label ErrorMessage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        GameInitType = new SimpleObjectProperty<>();           
    }
              
    public void SetErrorMessage(String Message)   
    {
        ErrorMessage.setText(Message);
    }

    public SimpleObjectProperty<MainMenu> getGameInitType() {
        return GameInitType;
    }

    @FXML
    private void NewGameAction(ActionEvent event) {
        GameInitType.set(MainMenu.NEW_GAME);
    }

    @FXML
    private void LoadGameAction(ActionEvent event) {
        GameInitType.set(MainMenu.LOAD_GAME);
        GameInitType.set(null);
    }
}
