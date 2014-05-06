/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;


import GameEnums.JoinGame;
import GameEnums.MainMenu;
import blackjackfx.ServerClasses.Events;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    
    public class Connected implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
            if (t1)
                GoToJoinScreen();
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
                      ScreenManager.GetInstance().getCreateGameCr().setBjGame(BlackJackGame);
                      Scene scene = ScreenManager.GetInstance().getCreateGameSc();
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
                            
                          String xmlData = new String(Files.readAllBytes(flOpen.toPath()));
                          BlackJackGame.CreateGameFromXML(xmlData);
                          GoToWaitingGamesScreen();
                        } 
                        catch (Exception ex) 
                        {
                            ScreenManager.GetInstance().getMainWinCr().SetErrorMessage(ex.getMessage());
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
            if (t1){
              StartGame();  
            }
                
            
           
        }
    }
    
    public class JoinType implements ChangeListener<JoinGame>{

        @Override
        public void changed(ObservableValue<? extends JoinGame> ov, JoinGame t, JoinGame t1) {
            if (t1.equals(JoinGame.CREATE_GAME))
                GoToMainScreen();
            else
                GoToWaitingGamesScreen();
        }
        
    }
    
        public class JoinNewGame implements ChangeListener<Boolean>{

        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
            if (t1)
                GoToWaitingGamesScreen();
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
                PrimaryStage.setOnCloseRequest(new HandleClose());
            }
            else
            {
                PrimaryStage.setScene(GameScene); 
                PrimaryStage.centerOnScreen();
                PrimaryStage.setTitle("Let's play BLACKJACK"); 
                
                PrimaryStage.setOnCloseRequest(new HandleClose());
            }
        }
    }
    
    public class HandleClose implements EventHandler<WindowEvent>{

        @Override
        public void handle(WindowEvent t) {
            ScreenManager.GetInstance().getGameScCr().getGameEvents().PlayerResign();
        }
        
    }
    
    public void StartGame(){
       ScreenManager.GetInstance().getGameScCr().setGameEvents(BlackJackGame);       
       Scene scene = ScreenManager.GetInstance().getGameSc();          
       PrimaryStage.setScene(scene); 
       PrimaryStage.centerOnScreen();
       PrimaryStage.setTitle("Let's play BLACKJACK");   
       PrimaryStage.setOnCloseRequest(new HandleClose());
    }
        
    public void EndGame(){
        Platform.exit();
    }
   
    public void GoToJoinScreen(){
        
        BlackJackGame = ScreenManager.GetInstance().getLoginScCr().getGameWSConnection();
        Scene scene = ScreenManager.GetInstance().getJoinSc();
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
        PrimaryStage.setTitle("Welcome to BLACKJACK");
    }
    
    public void GoToMainScreen()
    {
       Scene scene = ScreenManager.GetInstance().getMainWinSc();
       PrimaryStage.setScene(scene);
       PrimaryStage.show();
       PrimaryStage.setTitle("Welcome to BLACKJACK");
    }
    
    public void GoToWaitingGamesScreen()
    {
       Scene scene = ScreenManager.GetInstance().getWaitGameSc();
       ScreenManager.GetInstance().getWaitGameCr().setGameWS(BlackJackGame);
       PrimaryStage.setScene(scene);
       PrimaryStage.show();
       PrimaryStage.setTitle("Waiting Games");
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException
    {
       this.PrimaryStage = primaryStage;
       PrimaryStage.setResizable(false);
       ScreenManager.GetInstance().getMainWinCr().getGameInitType().addListener(new ChangeMainMenu());
       ScreenManager.GetInstance().getCreateGameCr().getFinishedInit().addListener(new JoinNewGame());   
       ScreenManager.GetInstance().getGameScCr().GetHideBidWindow().addListener(new BidAmount());
       ScreenManager.GetInstance().getGameScCr().getGameEnded().addListener(new GameEnded());
       ScreenManager.GetInstance().getLoginScCr().GetConnected().addListener(new Connected());
       ScreenManager.GetInstance().getJoinGameCr().getJoinGameType().addListener(new JoinType());
       ScreenManager.GetInstance().getWaitGameCr().getFinishJoinGame().addListener(new StartGame());
       Scene scene = ScreenManager.GetInstance().getLoginSc();
       PrimaryStage.setScene(scene);
       PrimaryStage.show();
       PrimaryStage.setTitle("Login to server");
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
