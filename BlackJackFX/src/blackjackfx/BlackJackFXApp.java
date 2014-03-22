/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.GameEngine;
import GameEnums.MainMenu;
import blackjackfx.Controllers.CreatePlayersScreenController;
import blackjackfx.Controllers.MainWindowController;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class BlackJackFXApp extends Application {
    
    Stage PrimaryStage;
    
    public class ChangeMainMenu implements ChangeListener<MainMenu>{

        @Override
        public void changed(ObservableValue<? extends MainMenu> ov, MainMenu oldValue, MainMenu NewValue)  {
            

            
            switch (NewValue)
               {
                   case NEW_GAME:
                   {
                      
                      GameEngine BlackJackGame = new GameEngine(); 
                      ScreenManager.GetInstance().getCrePlayerCr().setBjGame(BlackJackGame);
                      Scene scene = new Scene(ScreenManager.GetInstance().getCrePlayerSc());
                      PrimaryStage.setScene(scene); 
                   }
                   case LOAD_GAME:
                   {
                       
                   }
               }
           }      
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException
    {
       this.PrimaryStage = primaryStage;
       ScreenManager.GetInstance().getMainWinCr().getGameInitType().addListener(new ChangeMainMenu());
          
       Scene scene = new Scene(ScreenManager.GetInstance().getMainWinSc());
       PrimaryStage.setScene(scene);
       PrimaryStage.show();
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
