/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsoleApp;

import ConsoleApp.UserOptions.*; 
import EngineLogic.Communicable.PlayerAction;
import EngineLogic.Communicable.RoundAction;

/**
 *
 * @author miri
 */
public class MenuMessages {   

    public static void OpeningMessage(){
        
        System.out.println("*********************************************");
        System.out.println("Welcome to BlackJack");
        System.out.println("*********************************************");
    }
    
    public static void MainMenuMessage(){
        
        System.out.println("Which game do you want?");
        
        for (MainMenu option: MainMenu.values())
            System.out.println(option.ordinal() + "- " + 
                               option.getDescription());
    }
    
    public static void SecondaryMenuMessage(){
        
      System.out.println("What do you wish to do?");
        
        for (SecondaryMenu option: SecondaryMenu.values())
            System.out.println(option.ordinal() + "- " + 
                               option.getDescription()); 
    }
    
    public static void PlayerActionMessage(){
        
      System.out.println("What is your next action?");
        
        for (PlayerAction option: PlayerAction.values())
            System.out.println(option.ordinal() + "- " + 
                               option.getDescription()); 
    }
    
    public static void NewPlayerMessage(){
        
      System.out.println("Which player do you want to add?");
        
        for (NewPlayer option: NewPlayer.values())
            System.out.println(option.ordinal() + "- " + 
                               option.getDescription()); 
    }
    
    public static void RoundActionMessage(){
        
      System.out.println("What do you wish to do?");
        
        for (RoundAction option: RoundAction.values())
            System.out.println(option.ordinal() + "- " + 
                               option.getDescription()); 
    }
    
    public static void LoadGameMessage(){
        
        System.out.println("Enter xml file path");
    }
  
}
