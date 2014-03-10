/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsoleApp;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author miri
 */
public class UserOptions {
    
       
    public static enum MainMenu{
        NEW_GAME("New Game"), LOAD_GAME("Load Game");
        
        private final String Description;
        private static final int Size = MainMenu.values().length;
        
        MainMenu(String Description){
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }

        public static int getSize() {
            return Size;
        }      
    }
   
    public static enum SecondaryMenu{
        ADD_PLAYER("Add Player"), START_GAME("Start Game");
        
        private final String Description;
        private static final int Size = MainMenu.values().length;
        
        SecondaryMenu(String Description){
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }

        public static int getSize() {
            return Size;
        }      
    }
        
    public static enum NewPlayer{
        
        HUMAN_PLAYER("Human Player"),COMP_PLAYER("Computer Player");
        
        private final String Description;
        
        private static final int Size = NewPlayer.values().length;
        
        NewPlayer(String Description){
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }

        public static int getSize() {
            return Size;
        }     
    }

    public static Integer UserIntChoice(int MaxValue){
        
        Integer Choice = 0;
        Boolean ValidInput;
        Scanner Scanner = new Scanner(System.in);
        
        do{   
            ValidInput = true;
            System.out.println("Enter your choice");
        
            try {
		   Choice = Scanner.nextInt();
	    } catch (InputMismatchException exception) {
		System.out.println("This is not a valid number!!!");
		ValidInput = false;
                Scanner.nextLine();
	    }
        }while ( (ValidInput == false) || (Choice > MaxValue));
        
        return Choice;
    }
    
     public static Double UserDoubleInput(){
        
        Double userInput = 0.0;
        Boolean ValidInput;
        Scanner Scanner = new Scanner(System.in);
        
        do{   
            ValidInput = true;
        
            try {
		   userInput = Scanner.nextDouble();
	    } catch (InputMismatchException exception) {
		System.out.println("This is not a valid number!!!");
		ValidInput = false;
                Scanner.nextLine();
	    }
        }while (ValidInput == false);
        
        return userInput;
    }
    
    public static String FilePathInput(){
        
        String filePathString;
        File file;
        Scanner scanner = new Scanner(System.in);
        
        do {      
            filePathString =  scanner.nextLine();
            file = new File(filePathString);
        }while((!file.exists() || file.isDirectory()));
        
        return filePathString;
    }
    
    public static String FilePathOutput(){
        
        String filePathString;
        boolean OverwriteFlag = true;
        File file;
        Scanner scanner = new Scanner(System.in);
        
        do {      
            filePathString =  scanner.nextLine();
            file = new File(filePathString);
            
            if (file.exists()){
                System.out.println("file already exists. overewrite it?");
                OverwriteFlag = scanner.nextBoolean();
            }
        }while(!OverwriteFlag || file.isDirectory());
        
        return filePathString;      
    }
}
