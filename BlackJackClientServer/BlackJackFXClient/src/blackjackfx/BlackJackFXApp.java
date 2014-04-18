/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;


import GameEnums.MainMenu;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author yakir
 */
public class BlackJackFXApp extends Application {
    
    Stage PrimaryStage;
    Events BlackJackGame;

    public class GameEnded implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
            if (t1)
                EndGame();
        }
    }
    
    public class ChangeMainMenu implements ChangeListener<MainMenu>{

        @Override
        public void changed(ObservableValue<? extends MainMenu> ov, MainMenu oldValue, MainMenu NewValue)  {
            
            if(NewValue != null)
            {
                switch (NewValue)
                {
                   case NEW_GAME:
                   {
                      
                      BlackJackGame = new GameEngine(); 
                      ScreenManager.GetInstance().getCreatePlayerCr().setBjGame(BlackJackGame);
                      Scene scene = ScreenManager.GetInstance().getCreatePlayerSc();
                      PrimaryStage.setScene(scene); 
                      PrimaryStage.setTitle("Create Players");
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
                          StartGame();
                        } 
                        catch (Exception ex) 
                        {
                            ScreenManager.GetInstance().getMainWinCr().SetErrorMessage("Error choose another file");
                        } 
                        break;
                   }
                }
              }
           }      
    }
    
    
    public class StartGame implements ChangeListener<Boolean>{   

        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) 
        {
            StartGame();
           
        }
    }
    
    public class BidAmount implements ChangeListener<Boolean>{   

        Scene GameScene;

        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) 
        {
            if (!t1){
                GameScene = PrimaryStage.getScene();

                Scene scene = ScreenManager.GetInstance().getBidSc();          
                PrimaryStage.setScene(scene); 
                PrimaryStage.centerOnScreen();
                PrimaryStage.setTitle("Enter Bid");
            }
            else
            {
                PrimaryStage.setScene(GameScene); 
                PrimaryStage.centerOnScreen();
                PrimaryStage.setTitle("Let's play BLACKJACK");              
            }
        }
    }
    
    public void StartGame(){
       ScreenManager.GetInstance().getGameScCr().setBJGame(BlackJackGame);
       Scene scene = ScreenManager.GetInstance().getGameSc();          
       PrimaryStage.setScene(scene); 
       PrimaryStage.centerOnScreen();
       PrimaryStage.setTitle("Let's play BLACKJACK");           
    }
        
    public void EndGame(){
        Platform.exit();
    }
        
    @Override
    public void start(Stage primaryStage) throws IOException
    {
       this.PrimaryStage = primaryStage;
       PrimaryStage.setResizable(false);
       ScreenManager.GetInstance().getMainWinCr().getGameInitType().addListener(new ChangeMainMenu());
       ScreenManager.GetInstance().getCreatePlayerCr().getFinishedInit().addListener(new StartGame());   
       ScreenManager.GetInstance().getGameScCr().GetHideBidWindow().addListener(new BidAmount());
       ScreenManager.GetInstance().getGameScCr().getGameEnded().addListener(new GameEnded());
       Scene scene = ScreenManager.GetInstance().getMainWinSc();
       PrimaryStage.setScene(scene);
       PrimaryStage.show();
       PrimaryStage.setTitle("Welcome to BLACKJACK");
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
