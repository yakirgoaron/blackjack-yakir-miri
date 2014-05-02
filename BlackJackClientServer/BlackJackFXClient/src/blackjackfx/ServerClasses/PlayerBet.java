/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.ServerClasses;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Yakir
 */
public class PlayerBet {
    private List<Card> BetCards;
    private float BetWage;

   
    public PlayerBet(List<Card> BetCards,float BetWage)    
    {
        this.BetCards = new ArrayList<>(BetCards.size() + 1);
        Collections.copy(this.BetCards, BetCards);
        this.BetWage = BetWage;
    }
    
    public List<Card> getBetCards() {
        return BetCards;
    }

    public float getBetWage() {
        return BetWage;
    }
    
    
}
