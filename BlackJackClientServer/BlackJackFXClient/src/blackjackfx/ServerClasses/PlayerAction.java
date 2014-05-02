/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.ServerClasses;

/**
 *
 * @author Yakir
 */
public enum PlayerAction{
        
        DOUBLE("Double"), HIT("Hit"), SPLIT("Split"), STAND("Stand");
        
        private final String Description;
        
        PlayerAction(String Description){
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }
  
    }
