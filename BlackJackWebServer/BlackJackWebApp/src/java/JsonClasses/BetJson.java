/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JsonClasses;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miri
 */
public class BetJson {
    protected List<CardJson> BetCards;
    protected float BetWage; 

    public BetJson(List<CardJson> BetCards, float BetWage) {
        this.BetCards = new ArrayList<>();
        this.BetCards.addAll(BetCards);
        this.BetWage = BetWage;
    }

    public List<CardJson> getBetCards() {
        return BetCards;
    }

    public float getBetWage() {
        return BetWage;
    }

    public void setBetWage(float BetWage) {
        this.BetWage = BetWage;
    }

    public void setBetCards(List<CardJson> BetCards) {
        this.BetCards = BetCards;
    }
    
    
    
}
