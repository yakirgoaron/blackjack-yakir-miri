/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsoleApp;

import ConsoleApp.UserOptions.MainMenu;
import ConsoleApp.UserOptions.NewPlayer;
import ConsoleApp.UserOptions.SecondaryMenu;
import GameEngine.Exception.RoundStartedException;
import GameEngine.Exception.TooManyPlayersException;
import GameEngine.GameEngine;
import java.util.Scanner;

/**
 *
 * @author miri
 */
public class BlackJack {
    
    GameEngine GameEng;
    
    
    public static void main(String[] args) {
    
        MenuMessages.OpeningMessage();
        BlackJack BJGame = CreateBJGame();      
        BJGame.PlayGame();
    }
    
    
    private static BlackJack CreateBJGame()
    {
        BlackJack BJGame = NewOrLoadGame();
        BJGame.AddPlayers();
        
        return BJGame;
    }
    
    // C`tor for new game
    private BlackJack() {
      GameEng = new GameEngine();  
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
                // TODO: xml
                break;
            }          
        }         
        return BJGame;
    }
    
    private static BlackJack NewGame(){                        
        return new BlackJack();     
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
            IntUserChoice = UserOptions.UserIntChoice(SecondaryMenu.getSize());
            EnumUserChoice = SecondaryMenu.values()[IntUserChoice];            
        }
        }catch(TooManyPlayersException exception){
            System.out.println("Too many players - can`t add another player");
        }catch(RoundStartedException exception){
            System.out.println("In the middle of the round - can`t add another player");
        }
    }
        
   
    private void CreatePlayers()throws TooManyPlayersException, RoundStartedException{
       
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
    
    private void AddHumanPlayer() throws TooManyPlayersException, RoundStartedException{
        
        String PlayerName;
        Scanner Scanner = new Scanner(System.in);
        System.out.println("Enter player name - ");
        PlayerName = Scanner.nextLine();   
        GameEng.AddPlayer(PlayerName);       
    }
    
    private void AddCompPlayer() throws TooManyPlayersException, RoundStartedException{
        GameEng.AddPlayer();       
    }
               
    public void PlayGame(){
        GameEng.StartNewRound();
    }           
    
}
