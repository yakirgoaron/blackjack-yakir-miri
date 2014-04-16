
package ws.blackjack;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eventType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="eventType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GameStart"/>
 *     &lt;enumeration value="GameOver"/>
 *     &lt;enumeration value="GameWinner"/>
 *     &lt;enumeration value="PlayerResigned"/>
 *     &lt;enumeration value="NewRound"/>
 *     &lt;enumeration value="PlayerTurn"/>
 *     &lt;enumeration value="CardsDealt"/>
 *     &lt;enumeration value="PropmtPlayerToTakeAction"/>
 *     &lt;enumeration value="UserAction"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "eventType")
@XmlEnum
public enum EventType {

    @XmlEnumValue("GameStart")
    GAME_START("GameStart"),
    @XmlEnumValue("GameOver")
    GAME_OVER("GameOver"),
    @XmlEnumValue("GameWinner")
    GAME_WINNER("GameWinner"),
    @XmlEnumValue("PlayerResigned")
    PLAYER_RESIGNED("PlayerResigned"),
    @XmlEnumValue("NewRound")
    NEW_ROUND("NewRound"),
    @XmlEnumValue("PlayerTurn")
    PLAYER_TURN("PlayerTurn"),
    @XmlEnumValue("CardsDealt")
    CARDS_DEALT("CardsDealt"),
    @XmlEnumValue("PropmtPlayerToTakeAction")
    PROPMT_PLAYER_TO_TAKE_ACTION("PropmtPlayerToTakeAction"),
    @XmlEnumValue("UserAction")
    USER_ACTION("UserAction");
    private final String value;

    EventType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EventType fromValue(String v) {
        for (EventType c: EventType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
