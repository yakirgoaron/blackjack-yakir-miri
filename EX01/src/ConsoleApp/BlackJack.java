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
import org.xml.sax.SAXException;

/**
 *
 * @author miri
 */
public class BlackJack {
    
    GameEngine GameEng;
    BJCommunicate BJComm;
    
    
    public static void main(String[] args) throws RulesDosentAllowException, JAXBException {
    
        MenuMessages.OpeningMessage();
        BlackJack BJGame = CreateBJGame();     
        
        if (BJGame != null)
        {
            BJGame.StartGame();                       
        }
    }
    
    
    private static BlackJack CreateBJGame(){
       
        BlackJack BJGame = NewOrLoadGame();       
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
                                                    DuplicateCardException,       
                                                    SAXException{       
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
        
        BlackJack BJGame = new BlackJack();
        BJGame.AddPlayers();
        return  BJGame;
    } 
    
    private static BlackJack LoadGame() {     
       
        BlackJack BJGame = null;
        boolean FileValid;
        
        do{
            try{
                FileValid = true;
                MenuMessages.LoadGameMessage();
                String filePathString = UserOptions.FilePathInput();
                BJGame = new BlackJack(filePathString);               
            }
            catch(JAXBException | SAXException exception){
                System.out.println("file not suitable to expected strcture");
                FileValid = false;
            }
            catch (TooManyPlayersException exception){
                System.out.println("Too many players in file");
                FileValid = false;
            }
            catch (DuplicateCardException exception){
                System.out.println("File contains duplicate cards");
                FileValid = false;
            }                    
        }while(!FileValid);
        
        return BJGame;
    }
    
    
    private void AddPlayers(){
        
        int IntUserChoice;
        SecondaryMenu EnumUserChoice;
        
        try{
            do{
                CreatePlayer();
                MenuMessages.SecondaryMenuMessage();
                IntUserChoice = 
                        UserOptions.UserIntChoice(SecondaryMenu.getSize());
                EnumUserChoice = SecondaryMenu.values()[IntUserChoice];            
            }while (EnumUserChoice == SecondaryMenu.ADD_PLAYER);
        }catch(TooManyPlayersException exception){
            System.out.println("Too many players - can`t add another player");
        }
    }
        
   
    private void CreatePlayer()throws TooManyPlayersException{
       
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
               
    private void StartGame() throws JAXBException {
        GameEng.StartGame(BJComm);
    }
}
