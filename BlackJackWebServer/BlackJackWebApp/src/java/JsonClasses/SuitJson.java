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
public enum SuitJson {

    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES;

    public String value() {
        return name();
    }

    public static SuitJson fromValue(String v) {
        return valueOf(v);
    }

}