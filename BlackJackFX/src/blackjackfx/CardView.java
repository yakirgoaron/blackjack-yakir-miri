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
    
    public CardView(Card card){
        super();
        String ClassName = card.getRank().toString() + card.getSuit().toString();
        getStyleClass().add(ClassName);
    }
}
