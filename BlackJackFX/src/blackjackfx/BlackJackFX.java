/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.GameEngine;
import blackjackfx.Controllers.CreatePlayersScreenController;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author yakir
 */
public class BlackJackFX extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException
    {
       GameEngine BlackJackGame = new GameEngine();
       FXMLLoader fxmlLoader = new FXMLLoader();
       URL url = getClass().getResource("CreatePlayersScreen.fxml");
       fxmlLoader.setLocation(url);
       Parent root = (Parent)fxmlLoader.load(url.openStream());
       CreatePlayersScreenController CreatePlayers = (CreatePlayersScreenController) fxmlLoader.getController();
       
       CreatePlayers.setBjGame(BlackJackGame);
       Scene scene = new Scene(root);
       primaryStage.setScene(scene);
       primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
