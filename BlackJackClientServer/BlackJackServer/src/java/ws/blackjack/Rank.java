
package ws.blackjack;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rank.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="rank">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TWO"/>
 *     &lt;enumeration value="THREE"/>
 *     &lt;enumeration value="FOUR"/>
 *     &lt;enumeration value="FIVE"/>
 *     &lt;enumeration value="SIX"/>
 *     &lt;enumeration value="SEVEN"/>
 *     &lt;enumeration value="EIGHT"/>
 *     &lt;enumeration value="NINE"/>
 *     &lt;enumeration value="TEN"/>
 *     &lt;enumeration value="JACK"/>
 *     &lt;enumeration value="QUEEN"/>
 *     &lt;enumeration value="KING"/>
 *     &lt;enumeration value="ACE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "rank")
@XmlEnum
public enum Rank {

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

    public static Rank fromValue(String v) {
        return valueOf(v);
    }

}
