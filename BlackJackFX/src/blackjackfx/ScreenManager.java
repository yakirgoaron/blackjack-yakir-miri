/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import blackjackfx.Controllers.BidInputController;
import blackjackfx.Controllers.CreatePlayersScreenController;
import blackjackfx.Controllers.GameScreenController;
import blackjackfx.Controllers.LoadScreenController;
import blackjackfx.Controllers.MainWindowController;
import blackjackfx.Controllers.SaveScreenController;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author yakir
 */
public class ScreenManager 
{
     private CreatePlayersScreenController CrePlayerCr;
     private GameScreenController GameScCr;
     private LoadScreenController LoadScCr;
     private MainWindowController MainWinCr;
     private SaveScreenController SavScCr;
     private BidInputController   BidScCr;
     
     private Scene CrePlayerSc;
     private Scene GameSc;
     private Scene LoadSc;
     private Scene MainWinSc;
     private Scene SavSc;
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
        CrePlayerSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
        CrePlayerCr = (CreatePlayersScreenController) fxmlLoader.getController();
     }
     
     private void LoadGameScreen() throws IOException
     {
         LoadFxml("GameScreen.fxml");
         GameSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
         GameScCr = (GameScreenController) fxmlLoader.getController();
     }
     
     private void LoadScreenLoad() throws IOException
     {
         LoadFxml("LoadScreen.fxml");
         LoadSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
         LoadScCr = (LoadScreenController) fxmlLoader.getController();
     }
     
     private void LoadMainWindow() throws IOException
     {
         LoadFxml("MainWindow.fxml");
         MainWinSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
         MainWinCr = (MainWindowController) fxmlLoader.getController();
     }
     
     private void LoadSaveScreen() throws IOException
     {
         LoadFxml("SaveScreen.fxml");
         SavSc = new Scene((Parent)fxmlLoader.load(url.openStream()));     
         SavScCr = (SaveScreenController) fxmlLoader.getController();
     }
    
     private ScreenManager() throws IOException
     {
        LoadCreatePlayers();        
        LoadGameScreen();        
        LoadScreenLoad();        
        LoadMainWindow();        
        LoadSaveScreen();
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

    public CreatePlayersScreenController getCrePlayerCr() {
        return CrePlayerCr;
    }

    public GameScreenController getGameScCr() {
        return GameScCr;
    }

    public LoadScreenController getLoadScCr() {
        return LoadScCr;
    }

    public MainWindowController getMainWinCr() {
        return MainWinCr;
    }

    public SaveScreenController getSavScCr() {
        return SavScCr;
    }
    
    public BidInputController getBidScCr() {
        return BidScCr;
    }

    public Scene getCrePlayerSc() {
        return CrePlayerSc;
    }

    public Scene getGameSc() {
        return GameSc;
    }

    public Scene getLoadSc() {
        return LoadSc;
    }

    public Scene getMainWinSc() {
        return MainWinSc;
    }

    public Scene getSavSc() {
        return SavSc;
    }
    
    public Scene getBidSc() {
        return BidSc;
    }
     
}
