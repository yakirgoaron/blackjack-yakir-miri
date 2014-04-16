
package ws.blackjack;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for suit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="suit">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CLUBS"/>
 *     &lt;enumeration value="DIAMONDS"/>
 *     &lt;enumeration value="HEARTS"/>
 *     &lt;enumeration value="SPADES"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "suit")
@XmlEnum
public enum Suit {

    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES;

    public String value() {
        return name();
    }

    public static Suit fromValue(String v) {
        return valueOf(v);
    }

}
