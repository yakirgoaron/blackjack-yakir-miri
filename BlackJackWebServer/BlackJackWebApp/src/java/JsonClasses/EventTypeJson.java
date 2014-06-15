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
public enum EventTypeJson {

        GAME_START("GameStart"),
        GAME_OVER("GameOver"),
        GAME_WINNER("GameWinner"),
        PLAYER_RESIGNED("PlayerResigned"),
        NEW_ROUND("NewRound"),
        PLAYER_TURN("PlayerTurn"),
        CARDS_DEALT("CardsDealt"),
        PROMPT_PLAYER_TO_TAKE_ACTION("PromptPlayerToTakeAction"),
        USER_ACTION("UserAction");
        private final String value;

        EventTypeJson(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static EventTypeJson fromValue(String v) {
            for (EventTypeJson c: EventTypeJson.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }
    
