/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import blackjackfx.Controllers.BidInputController;
import blackjackfx.Controllers.CreatePlayersScreenController;
import blackjackfx.Controllers.GameScreenController;
import blackjackfx.Controllers.MainWindowController;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author yakir
 */
public class ScreenManager 
{
     private CreatePlayersScreenController CreatePlayerCr;
     private GameScreenController GameScCr;
     private MainWindowController MainWinCr;
     private BidInputController   BidScCr;
     
     private Scene CreatePlayerSc;
     private Scene GameSc;
     private Scene MainWinSc;
     private Scene BidSc;
     
     
     private FXMLLoader fxmlLoader;
     private URL url;
     
     private static ScreenManager Instance;
     
     private void LoadFxml(String NameScreen)
     {
         fxmlLoader = new FXMLLoader();
         url = getClass().getResource(NameScreen);  
         fxmlLoader.setLocation(url);
     }
     
     private void LoadBidScreen() throws IOException
     {
        LoadFxml("BidInput.fxml");
        BidSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
        BidScCr = (BidInputController) fxmlLoader.getController();
     }
     
     private void LoadCreatePlayers() throws IOException
     {
        LoadFxml("CreatePlayersScreen.fxml");
        CreatePlayerSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
        CreatePlayerCr = (CreatePlayersScreenController) fxmlLoader.getController();
     }
     
     private void LoadGameScreen() throws IOException
     {
         LoadFxml("GameScreen.fxml");
         GameSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
         GameScCr = (GameScreenController) fxmlLoader.getController();
     }
     
     private void LoadMainWindow() throws IOException
     {
         LoadFxml("MainWindow.fxml");
         MainWinSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
         MainWinCr = (MainWindowController) fxmlLoader.getController();
     }
    
     private ScreenManager() throws IOException
     {
        LoadCreatePlayers();        
        LoadGameScreen();             
        LoadMainWindow();        
        LoadBidScreen();
     }

     public static ScreenManager GetInstance()
     {       
         if(Instance == null)
         {
             try 
             {
                 Instance = new ScreenManager();
             }
             catch (IOException ex) 
             {
                 return null;
             }
         }
         return Instance;
     }

    public CreatePlayersScreenController getCreatePlayerCr() {
        return CreatePlayerCr;
    }

    public GameScreenController getGameScCr() {
        return GameScCr;
    }

    public MainWindowController getMainWinCr() {
        return MainWinCr;
    }
    
    public BidInputController getBidScCr() {
        return BidScCr;
    }

    public Scene getCreatePlayerSc() {
        return CreatePlayerSc;
    }

    public Scene getGameSc() {
        return GameSc;
    }

    public Scene getMainWinSc() {
        return MainWinSc;
    }
    
    public Scene getBidSc() {
        return BidSc;
    }
     
}
