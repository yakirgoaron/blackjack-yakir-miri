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
public enum RankJson {

    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE;

    public String value() {
        return name();
    }

    public static RankJson fromValue(String v) {
        return valueOf(v);
    }

}