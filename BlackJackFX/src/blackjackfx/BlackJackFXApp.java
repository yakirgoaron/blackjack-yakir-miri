/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.GameEngine;
import GameEnums.MainMenu;
import blackjackfx.Controllers.CreatePlayersScreenController;
import blackjackfx.Controllers.MainWindowController;
import java.io.File;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

/**
 *
 * @author yakir
 */
public class BlackJackFXApp extends Application {
    
    Stage PrimaryStage;
    GameEngine BlackJackGame;
    
    public class ChangeMainMenu implements ChangeListener<MainMenu>{

        @Override
        public void changed(ObservableValue<? extends MainMenu> ov, MainMenu oldValue, MainMenu NewValue)  {
            

            
            switch (NewValue)
               {
                   case NEW_GAME:
                   {
                      
                      BlackJackGame = new GameEngine(); 
                      ScreenManager.GetInstance().getCrePlayerCr().setBjGame(BlackJackGame);
                      Scene scene = new Scene(ScreenManager.GetInstance().getCrePlayerSc());
                      PrimaryStage.setScene(scene); 
                      break;
                   }
                   case LOAD_GAME:
                   {
               
                        FileChooser flChose = new FileChooser();
                        flChose.setTitle("Choose An Xml File to Load");
                        flChose.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
                        File flOpen = flChose.showOpenDialog(PrimaryStage);

                        try 
                        {
                          BlackJackGame = new GameEngine(flOpen.getPath());
                        //Scene scene = new Scene(ScreenManager.GetInstance().getLoadSc());
                        //PrimaryStage.setScene(scene);

                        } 
                        catch (Exception ex) 
                        {
                            ScreenManager.GetInstance().getMainWinCr().SetErrorMessage("Error chose another file");
                        } 
                        break;
                   }
                   
               }
           }      
    }
    
    
    public class StartGame implements ChangeListener<Boolean>{   

        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) 
        {
           ScreenManager.GetInstance().getGameScCr().setBJGame(BlackJackGame);
           Scene scene = new Scene(ScreenManager.GetInstance().getGameSc());          
           PrimaryStage.setScene(scene); 
           PrimaryStage.centerOnScreen();
        }
    }
    @Override
    public void start(Stage primaryStage) throws IOException
    {
       this.PrimaryStage = primaryStage;
       PrimaryStage.setResizable(false);
       ScreenManager.GetInstance().getMainWinCr().getGameInitType().addListener(new ChangeMainMenu());
       ScreenManager.GetInstance().getCrePlayerCr().getFinishedInit().addListener(new StartGame());   
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
