/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JsonClasses;

/**
 *
 * @author Yakir
 */
public class CardJson {

    protected RankJson rank;
    protected SuitJson suit;
    
    public CardJson(RankJson rank,SuitJson suit)
    {
        this.rank = rank;
        this.suit = suit;
    }
    
    public RankJson getRank() {
        return rank;
    }

    public void setRank(RankJson value) {
        this.rank = value;
    }

    public SuitJson getSuit() {
        return suit;
    }

    public void setSuit(SuitJson value) {
        this.suit = value;
    }

}
