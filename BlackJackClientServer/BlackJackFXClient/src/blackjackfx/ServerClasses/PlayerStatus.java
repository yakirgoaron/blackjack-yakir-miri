
package blackjackfx.ServerClasses;


public enum PlayerStatus {
    
    JOINED,
    ACTIVE,
    RETIRED;

    public String value() {
        return name();
    }

    public static PlayerStatus fromValue(String v) {
        return valueOf(v);
    }

}
