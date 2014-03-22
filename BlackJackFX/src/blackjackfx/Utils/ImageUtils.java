/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blackjackfx.Utils;

import java.io.File;
import java.net.URL;
import javafx.scene.image.Image;

/**
 *
 * @author yakir
 */
public class ImageUtils {
    private static final String RESOURCES_DIR = "/resources/";
    private static final String IMAGES_DIR = RESOURCES_DIR + "images/";
    private static final String IMAGE_EXTENSION = ".png";
    
    public static Image getImage (String filename){
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        
        if (!filename.endsWith(IMAGE_EXTENSION)){
            filename = filename + IMAGE_EXTENSION;
        }
        
        return new Image(ImageUtils.class.getResourceAsStream(IMAGES_DIR + filename));
    }
    public static Image getPlayerImage(String filename)
    {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        
        if (!filename.endsWith(IMAGE_EXTENSION)){
            filename = filename + IMAGE_EXTENSION;
        }
         return new Image(ImageUtils.class.getResourceAsStream(IMAGES_DIR + "players/" + filename));
    }
}
