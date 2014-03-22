/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEnums;

/**
 *
 * @author miri
 */
public enum MainMenu {
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
