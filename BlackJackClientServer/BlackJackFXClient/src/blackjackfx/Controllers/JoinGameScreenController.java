/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackfx.Controllers;

import GameEnums.JoinGame;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author miri
 */
public class JoinGameScreenController implements Initializable {
    
    private SimpleObjectProperty<JoinGame> JoinGameType;
    
    @FXML
    private Button btnCreateGame;
    @FXML
    private Button btnJoinGame;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JoinGameType = new SimpleObjectProperty<>();
    }    

    @FXML
    private void CreateGamePress(ActionEvent event) {
        JoinGameType.set(JoinGame.CREATE_GAME);
    }

    @FXML
    private void JoinGamePress(ActionEvent event) {
        JoinGameType.set(JoinGame.JOIN_GAME);
    }

    public SimpleObjectProperty<JoinGame> getJoinGameType() {
        return JoinGameType;
    }
    
    
}
