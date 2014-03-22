/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

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
     
     private Parent CrePlayerSc;
     private Parent GameSc;
     private Parent LoadSc;
     private Parent MainWinSc;
     private Parent SavSc;
     
     private FXMLLoader fxmlLoader;
     private URL url;
     
     private static ScreenManager Instance;
     
     private void LoadFxml(String NameScreen)
     {
         fxmlLoader = new FXMLLoader();
         url = getClass().getResource(NameScreen);  
         fxmlLoader.setLocation(url);
     }
     
     private void LoadCreatePlayers() throws IOException
     {
        LoadFxml("CreatePlayersScreen.fxml");
        CrePlayerSc = (Parent)fxmlLoader.load(url.openStream());     
        CrePlayerCr = (CreatePlayersScreenController) fxmlLoader.getController();
     }
     
     private void LoadGameScreen() throws IOException
     {
         LoadFxml("GameScreen.fxml");
         GameSc = (Parent)fxmlLoader.load(url.openStream());     
         GameScCr = (GameScreenController) fxmlLoader.getController();
     }
     
     private void LoadScreenLoad() throws IOException
     {
         LoadFxml("LoadScreen.fxml");
         LoadSc = (Parent)fxmlLoader.load(url.openStream());     
         LoadScCr = (LoadScreenController) fxmlLoader.getController();
     }
     
     private void LoadMainWindow() throws IOException
     {
         LoadFxml("MainWindow.fxml");
         MainWinSc = (Parent)fxmlLoader.load(url.openStream());     
         MainWinCr = (MainWindowController) fxmlLoader.getController();
     }
     
     private void LoadSaveScreen() throws IOException
     {
         LoadFxml("SaveScreen.fxml");
         SavSc = (Parent)fxmlLoader.load(url.openStream());     
         SavScCr = (SaveScreenController) fxmlLoader.getController();
     }
    
     private ScreenManager() throws IOException
     {
        LoadCreatePlayers();        
        LoadGameScreen();        
        LoadScreenLoad();        
        LoadMainWindow();        
        LoadSaveScreen();
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

    public Parent getCrePlayerSc() {
        return CrePlayerSc;
    }

    public Parent getGameSc() {
        return GameSc;
    }

    public Parent getLoadSc() {
        return LoadSc;
    }

    public Parent getMainWinSc() {
        return MainWinSc;
    }

    public Parent getSavSc() {
        return SavSc;
    }
}
