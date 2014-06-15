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
  public enum ActionJson
    {

        PLACE_BET("PlaceBet"),
        DOUBLE("Double"),
        HIT("Hit"),
        SPLIT("Split"),
        STAND("Stand");
        private final String value;

        ActionJson(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ActionJson fromValue(String v) {
            for (ActionJson c: ActionJson.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }
    }
    