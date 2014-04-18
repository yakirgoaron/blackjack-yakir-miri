/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackfx;

import javafx.scene.image.ImageView;
import game.client.ws.Card;

/**
 *
 * @author miri
 */
public class CardView extends ImageView{
    
    private Card cdRef;
    public CardView(Card card){
        super();
        String ClassName = card.getRank().toString() + card.getSuit().toString();
        cdRef = card;
        getStyleClass().add(ClassName);
    }

    public Card getCdRef() {
        return cdRef;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if(obj instanceof  CardView)
        {
            return cdRef.equals(((CardView)obj).cdRef);
        }
        return false;
    }
     
}
