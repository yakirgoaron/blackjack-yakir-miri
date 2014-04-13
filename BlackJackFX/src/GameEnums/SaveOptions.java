/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEnums;

/**
 *
 * @author miri
 */
public enum SaveOptions{
    SAVE("Save Game"), SAVE_AS("Save As");

    private final String Description;
    private static final int Size = SaveOptions.values().length;

    SaveOptions(String Description){
        this.Description = Description;
    }

    public String getDescription() {
        return Description;
    }

    public static int getSize() {
        return Size;
    }      
}
   
