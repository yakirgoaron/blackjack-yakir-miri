/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEnums;

/**
 *
 * @author miri
 */
public enum JoinGame {
        CREATE_GAME("Create Game"), JOIN_GAME("Join Game");
        
        private final String Description;
        private static final int Size = JoinGame.values().length;
        
        JoinGame(String Description){
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }

        public static int getSize() {
            return Size;
        }    
}
