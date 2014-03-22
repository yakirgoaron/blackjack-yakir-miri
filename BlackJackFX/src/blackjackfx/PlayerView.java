/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx;

import blackjackfx.Utils.ImageUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.VBox;

/**
 *
 * @author yakir
 */
public class PlayerView extends VBox{

    public PlayerView(String title, boolean isHuman) {
        setSpacing(10);
        setAlignment(Pos.CENTER);
        getChildren().addAll(createImage(isHuman), createLabel(title));
    }
    
    private Label createLabel(String title){
        return new Label(title);
    }
    
    private ImageView createImage(boolean isHuman){
        return ImageViewBuilder
                .create()
                .image(getImage(isHuman))
                .build();
    }

    private Image getImage(boolean isHuman) {
        String filename = isHuman ? "HumanPlayer" : "CompPlayer";
        return ImageUtils.getPlayerImage(filename);
    }
}