//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.03.15 at 01:52:33 PM IST 
//


package EngineLogic.XmlClasses;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Suit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Suit">
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
@XmlType(name = "Suit")
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
