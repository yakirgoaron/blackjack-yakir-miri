
package blackjackfx.ServerClasses;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


public enum PlayerStatus {

    ACTIVE,
    RETIRED;

    public String value() {
        return name();
    }

    public static PlayerStatus fromValue(String v) {
        return valueOf(v);
    }

}
