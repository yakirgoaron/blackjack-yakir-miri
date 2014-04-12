/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackfx;

import EngineLogic.Card;
import blackjackfx.Utils.ImageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;

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
