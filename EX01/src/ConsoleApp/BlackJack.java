/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsoleApp;

import ConsoleApp.UserOptions.MainMenu;
import ConsoleApp.UserOptions.NewPlayer;
import ConsoleApp.UserOptions.SecondaryMenu;
import EngineLogic.Exception.DuplicateCardException;
import EngineLogic.Exception.RulesDosentAllowException;
import EngineLogic.Exception.TooManyPlayersException;
import EngineLogic.GameEngine;
import java.util.Scanner;
import javax.xml.bind.JAXBException;

/**
 *
 * @author miri
 */
public class BlackJack {
    
    GameEngine GameEng;
    BJCommunicate BJComm;
    
    
    public static void main(String[] args) throws RulesDosentAllowException {
    
        MenuMessages.OpeningMessage();
        BlackJack BJGame = CreateBJGame();     
        
        if (BJGame != null)
            BJGame.StartGame();           
    }
    
    
    private static BlackJack CreateBJGame() {
        BlackJack BJGame = NewOrLoadGame();
        
        if (BJGame != null)
            BJGame.AddPlayers();
        
        return BJGame;
    }
    
    // C`tor for new game
    private BlackJack() {
      GameEng = new GameEngine();  
      BJComm = new BJCommunicate();
    }
    
    // C`tor for load game
    private BlackJack(String filePathString) throws JAXBException, 
                                                    TooManyPlayersException,       
                                                    DuplicateCardException{       
        GameEng = new GameEngine(filePathString);
        BJComm = new BJCommunicate();
    }
        
    private static BlackJack NewOrLoadGame(){
        
        int IntUserChoice;
        MainMenu EnumUserChoice;
        BlackJack BJGame = null;
               
        MenuMessages.MainMenuMessage();
        IntUserChoice = UserOptions.UserIntChoice(MainMenu.getSize());
        EnumUserChoice = MainMenu.values()[IntUserChoice];
        
        switch (EnumUserChoice) {
            case NEW_GAME:
            {
                BJGame = NewGame();
                break;  
            }
            case LOAD_GAME:
            {
                BJGame = LoadGame();
                break;
            }          
        }         
        return BJGame;
    }
    
    private static BlackJack NewGame(){                        
        return new BlackJack();     
    } 
    
    private static BlackJack LoadGame() {     
       
        BlackJack BJGame = null;
        MenuMessages.LoadGameMessage();
        String filePathString = UserOptions.FilePathInput();
        
        try{
           BJGame = new BlackJack(filePathString);
        }
        catch(JAXBException exception){
            System.out.println("file not suitable to expected strcture");
        }
        catch (TooManyPlayersException exception){
            System.out.println("Too many players in file");
        }
        catch (DuplicateCardException exception){
            System.out.println("File contains duplicate cards");
        }
        
        return BJGame;
    }
    
    
    private void AddPlayers(){
        
        int IntUserChoice;
        SecondaryMenu EnumUserChoice;
               
        MenuMessages.SecondaryMenuMessage();
        IntUserChoice = UserOptions.UserIntChoice(SecondaryMenu.getSize());
        EnumUserChoice = SecondaryMenu.values()[IntUserChoice];
        
        try{
            while (EnumUserChoice == SecondaryMenu.ADD_PLAYER) {

                CreatePlayers();
                MenuMessages.SecondaryMenuMessage();
                IntUserChoice = 
                        UserOptions.UserIntChoice(SecondaryMenu.getSize());
                EnumUserChoice = SecondaryMenu.values()[IntUserChoice];            
            }
        }catch(TooManyPlayersException exception){
            System.out.println("Too many players - can`t add another player");
        }
    }
        
   
    private void CreatePlayers()throws TooManyPlayersException{
       
        int IntUserChoice;
        NewPlayer EnumUserChoice;
        MenuMessages.NewPlayerMessage();
        
        IntUserChoice = UserOptions.UserIntChoice(NewPlayer.getSize());
        EnumUserChoice = NewPlayer.values()[IntUserChoice];
        
        switch (EnumUserChoice) {
            case HUMAN_PLAYER:
            {
                AddHumanPlayer();              
                break;  
            }
            case COMP_PLAYER:
            {
                AddCompPlayer();               
                break;
            }          
        }                        
    }
    
    private void AddHumanPlayer() throws TooManyPlayersException{
        
        String PlayerName;
        Scanner Scanner = new Scanner(System.in);
        System.out.println("Enter player name - ");
        PlayerName = Scanner.nextLine();   
        GameEng.AddPlayer(PlayerName);       
    }
    
    private void AddCompPlayer() throws TooManyPlayersException{
        GameEng.AddPlayer();       
    }
               
    private void StartGame() {
        GameEng.StartGame(BJComm);
    }
}
