/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Controllers;

import blackjackfx.Events;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Yakir
 */
public class LoginScreenController implements Initializable {
    @FXML
    private TextField txtServerName;
    @FXML
    private TextField txtPortValue;
    @FXML
    private Button Connect;
    @FXML
    private Label ErrorDisplay;
    private Events GameWSConnection;
    
    
    private SimpleBooleanProperty Connected;

    /**
     * Initializes the controller class.
     */
    
    public SimpleBooleanProperty GetConnected()
    {
        return Connected;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connected = new SimpleBooleanProperty();
        GameWSConnection = null;
    }    

    private void showError(String message) 
    {
        ErrorDisplay.textProperty().setValue(message);
        FadeTransition animation = FadeTransitionBuilder.create()
                    .node(ErrorDisplay)
                    .duration(Duration.seconds(0.3))
                    .fromValue(0.0)
                    .toValue(1.0)
                    .build();
        animation.play();          
    }
    
    @FXML
    private void ConnectToServer(ActionEvent event) {
        try {
            GameWSConnection = new Events(txtServerName.getText(), txtPortValue.getText());
            Connected.set(true);
        } catch (MalformedURLException ex) {
            showError(ex.getMessage());
        }
    }
    
}
